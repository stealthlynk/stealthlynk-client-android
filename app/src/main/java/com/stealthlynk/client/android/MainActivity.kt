package com.stealthlynk.client.android

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.stealthlynk.client.android.ui.StealthLynkApp
import com.stealthlynk.client.android.ui.theme.StealthLynkTheme
import com.stealthlynk.client.android.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()
    private var vpnPermissionGranted by mutableStateOf(false)

    // VPN Permission request launcher
    private val vpnPermissionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        vpnPermissionGranted = result.resultCode == RESULT_OK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check and request VPN permission
        checkVpnPermission()
        
        // Handle VLESS URL intent
        handleIntent(intent)

        setContent {
            StealthLynkTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass in the permission state to the app
                    val isConnected by viewModel.isConnected.collectAsState()
                    val connectionState by viewModel.connectionState.collectAsState()
                    val servers by viewModel.servers.collectAsState()
                    val activeServerId by viewModel.activeServerId.collectAsState()
                    val currentIp by viewModel.currentIp.collectAsState()
                    
                    StealthLynkApp(
                        isConnected = isConnected,
                        connectionState = connectionState,
                        vpnPermissionGranted = vpnPermissionGranted,
                        servers = servers,
                        activeServerId = activeServerId,
                        currentIp = currentIp,
                        onRequestVpnPermission = { checkVpnPermission() },
                        onConnect = { viewModel.connect(this) },
                        onDisconnect = { viewModel.disconnect(this) },
                        onAddServer = { url -> lifecycleScope.launch { viewModel.addServer(url) } },
                        onDeleteServer = { serverId -> lifecycleScope.launch { viewModel.deleteServer(serverId) } },
                        onSetActiveServer = { serverId -> lifecycleScope.launch { viewModel.setActiveServer(serverId) } },
                        onToggleSmartConnect = { enabled -> viewModel.setAutoFailover(enabled) }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        // Handle VLESS URLs when app is opened from a link
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            uri?.let {
                if (it.scheme == "vless") {
                    lifecycleScope.launch {
                        val serverUrl = it.toString()
                        viewModel.addServer(serverUrl)
                    }
                }
            }
        }
    }

    private fun checkVpnPermission() {
        val vpnIntent = VpnService.prepare(this)
        if (vpnIntent != null) {
            vpnPermissionLauncher.launch(vpnIntent)
        } else {
            // VPN permission already granted
            vpnPermissionGranted = true
        }
    }
}
