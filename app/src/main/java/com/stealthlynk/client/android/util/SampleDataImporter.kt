package com.stealthlynk.client.android.util

import android.content.Context
import com.stealthlynk.client.android.data.repository.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlinx.coroutines.flow.first
import java.io.IOException
import kotlinx.coroutines.flow.first

/**
 * Helper class to import sample server data when the app is first installed
 */
class SampleDataImporter(private val context: Context) {

    /**
     * Import sample VLESS URL if no servers exist in the repository
     */
    suspend fun importSampleDataIfNeeded(serverRepository: ServerRepository) {
        withContext(Dispatchers.IO) {
            try {
                // Check if there are existing servers
                val servers = serverRepository.getServers().first()
                if (servers.isEmpty()) {
                    Timber.d("No existing servers found. Importing sample data...")
                    
                    // Read sample VLESS URL from assets
                    val sampleVlessUrl = context.assets.open("sample_vless_url.txt")
                        .bufferedReader()
                        .use { it.readText() }
                        .trim()
                    
                    // Add sample server to repository
                    if (sampleVlessUrl.isNotEmpty()) {
                        serverRepository.addServer(sampleVlessUrl)
                        Timber.d("Sample server imported successfully")
                    }
                } else {
                    Timber.d("Existing servers found, skipping sample data import")
                }
            } catch (e: IOException) {
                Timber.e(e, "Failed to import sample data")
            }
        }
    }
}
