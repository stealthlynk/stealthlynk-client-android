package com.stealthlynk.client.android.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stealthlynk.client.android.data.model.Server
import com.stealthlynk.client.android.data.model.VlessConfig
import com.stealthlynk.client.android.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.UUID

class ServerRepository(private val context: Context) {
    private val gson = Gson()
    
    // Keys for DataStore
    private val SERVERS_KEY = stringPreferencesKey("servers")
    private val ACTIVE_SERVER_KEY = stringPreferencesKey("active_server")

    // Get all servers
    fun getServers(): Flow<List<Server>> = context.dataStore.data.map { preferences ->
        val serversJson = preferences[SERVERS_KEY] ?: "[]"
        val type = object : TypeToken<List<Server>>() {}.type
        gson.fromJson(serversJson, type) ?: emptyList()
    }
    
    // Get active server ID
    fun getActiveServerId(): Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ACTIVE_SERVER_KEY]
    }
    
    // Save servers
    suspend fun saveServers(servers: List<Server>) {
        context.dataStore.edit { preferences ->
            preferences[SERVERS_KEY] = gson.toJson(servers)
        }
    }
    
    // Set active server
    suspend fun setActiveServer(serverId: String) {
        context.dataStore.edit { preferences ->
            preferences[ACTIVE_SERVER_KEY] = serverId
        }
    }
    
    // Add a server
    suspend fun addServer(serverUrl: String): Result<Server> {
        return try {
            val server = parseVlessUrl(serverUrl)
            val currentServers = getServers().first().toMutableList()
            currentServers.add(server)
            saveServers(currentServers)
            
            // If this is the first server, set it as active
            if (currentServers.size == 1) {
                setActiveServer(server.id)
            }
            
            Result.success(server)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Delete a server
    suspend fun deleteServer(serverId: String): Result<Boolean> {
        return try {
            val currentServers = getServers().first().toMutableList()
            val activeServerId = getActiveServerId().first()
            
            // Remove the server
            currentServers.removeIf { it.id == serverId }
            saveServers(currentServers)
            
            // If we removed the active server, set a new one or clear it
            if (activeServerId == serverId) {
                if (currentServers.isNotEmpty()) {
                    setActiveServer(currentServers[0].id)
                } else {
                    context.dataStore.edit { preferences ->
                        preferences.remove(ACTIVE_SERVER_KEY)
                    }
                }
            }
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Get active server
    suspend fun getActiveServer(): Server? {
        val activeServerId = getActiveServerId().first() ?: return null
        val servers = getServers().first()
        return servers.find { it.id == activeServerId }
    }
    
    // Parse VLESS URL to create a Server object
    private fun parseVlessUrl(vlessUrl: String): Server {
        // Clean up the URL
        val cleanUrl = vlessUrl.trim().replace("[\r\n]".toRegex(), "")
        
        // Extract basic components with regex
        val basicRegex = "^vless://([^@]+)@([^:]+):(\\d+)".toRegex()
        val basicMatch = basicRegex.find(cleanUrl)
            ?: throw IllegalArgumentException("Invalid VLESS URL format")
        
        val (id, add, portStr) = basicMatch.destructured
        val port = portStr.toInt()
        
        // Extract query parameters and remark
        var queryString = ""
        var remark = ""
        
        // Find the query string part
        val queryStart = cleanUrl.indexOf("?", basicMatch.value.length)
        if (queryStart != -1) {
            // Find the remark part
            val remarkStart = cleanUrl.indexOf("#", queryStart)
            if (remarkStart != -1) {
                queryString = cleanUrl.substring(queryStart, remarkStart)
                remark = cleanUrl.substring(remarkStart + 1)
            } else {
                queryString = cleanUrl.substring(queryStart)
            }
        } else {
            // Check for remark without query
            val remarkStart = cleanUrl.indexOf("#", basicMatch.value.length)
            if (remarkStart != -1) {
                remark = cleanUrl.substring(remarkStart + 1)
            }
        }
        
        // Parse query parameters
        val params = mutableMapOf<String, String>()
        if (queryString.isNotEmpty()) {
            val queryPairs = queryString.substring(if (queryString.startsWith("?")) 1 else 0).split("&")
            for (pair in queryPairs) {
                val idx = pair.indexOf("=")
                if (idx > 0) {
                    val key = pair.substring(0, idx)
                    val value = pair.substring(idx + 1)
                    params[key] = URLDecoder.decode(value, StandardCharsets.UTF_8.name())
                }
            }
        }
        
        // Create the base config
        val vlessConfig = VlessConfig(
            id = id, // User ID
            add = add, // Address/hostname
            port = port,
            type = params["type"] ?: "tcp", // Connection type
            encryption = params["encryption"] ?: "none", // Encryption method
            protocol = "vless", // Protocol identifier
            ps = URLDecoder.decode(remark, StandardCharsets.UTF_8.name()) 
                .ifEmpty { "Server $add:$port" }, // Server name from remark
            net = params["type"] ?: "tcp", // Network type
            tls = when {
                params.containsKey("security") -> params["security"] ?: "none"
                params.containsKey("tls") -> "tls"
                else -> "none"
            }, // TLS setting
            sni = params["sni"] ?: params["host"] ?: "", // SNI value
            fp = params["fp"] ?: "chrome", // TLS fingerprint
            path = params["path"] ?: "/", // Path value
            peer = params["peer"] ?: "", // Server name for TLS
            flow = params["flow"] ?: "" // Flow setting for XTLS Vision
        )
        
        // Handle Reality protocol
        val realityTls = vlessConfig.tls == "reality" || 
                (params.containsKey("security") && params["security"] == "reality") ||
                (params.containsKey("pbk") && params.containsKey("sid")) ||
                (params.containsKey("pbk") && params.containsKey("tls"))
        
        val updatedVlessConfig = if (realityTls) {
            vlessConfig.copy(
                tls = "reality",
                pbk = params["pbk"] ?: "",
                sid = params["sid"] ?: "",
                spx = params["spx"] ?: "/"
            )
        } else {
            vlessConfig
        }
        
        // Create server object
        return Server(
            id = UUID.randomUUID().toString(),
            protocol = "vless",
            name = updatedVlessConfig.ps,
            address = add,
            port = port,
            rawConfig = updatedVlessConfig,
            addedAt = java.time.Instant.now().toString()
        )
    }
}
