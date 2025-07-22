package com.stealthlynk.client.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stealthlynk.client.android.StealthLynkApplication
import com.stealthlynk.client.android.service.XrayVpnService
import com.stealthlynk.client.android.util.XrayBinaryChecker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Component for testing VPN service functionality
 */
@Composable
fun ServiceTester(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    
    var testResult by remember { mutableStateOf("No test run yet") }
    var testRunning by remember { mutableStateOf(false) }
    
    val isVpnRunning by XrayVpnService.isRunning.collectAsState(initial = false)
    val vpnState by XrayVpnService.connectionState.collectAsState(initial = "UNKNOWN")
    
    val app = StealthLynkApplication.instance
    val serverRepository = app.serverRepository
    val xrayConfigRepository = app.xrayConfigRepository

    Surface(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Service Diagnostics",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "VPN Status: $vpnState",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    testRunning = true
                    scope.launch {
                        try {
                            testResult = "Testing xray binary..."
                            val xrayBinaryChecker = XrayBinaryChecker(app)
                            val binaryOk = xrayBinaryChecker.checkXrayBinary()
                            
                            if (binaryOk) {
                                testResult = "✅ Xray binary check passed\n\nTesting config generation..."
                                
                                // Get servers and check if we have any
                                val servers = serverRepository.getServers().first()
                                if (servers.isEmpty()) {
                                    testResult = "❌ No servers found. Add a server first."
                                    testRunning = false
                                    return@launch
                                }
                                
                                // Generate config for first server
                                val configPath = xrayConfigRepository.generateAndSaveXrayConfig(servers.first())
                                testResult = "$testResult\n✅ Config generated at $configPath\n\nAll tests passed!"
                            } else {
                                testResult = "❌ Xray binary check failed. Please download and install the binary."
                            }
                        } catch (e: Exception) {
                            Timber.e(e, "Error during service test")
                            testResult = "❌ Test failed: ${e.message}"
                        } finally {
                            testRunning = false
                        }
                    }
                },
                enabled = !testRunning && !isVpnRunning
            ) {
                Text("Run Diagnostics")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = testResult,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            
            if (testRunning) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Test running...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = {
                    scope.launch {
                        try {
                            val xrayBinaryChecker = XrayBinaryChecker(app)
                            xrayBinaryChecker.checkXrayBinary()
                            testResult = "Xray binary extraction triggered.\nCheck logs for details."
                        } catch (e: Exception) {
                            testResult = "Failed to extract binary: ${e.message}"
                        }
                    }
                },
                enabled = !isVpnRunning
            ) {
                Text("Extract Binary")
            }
        }
    }
}
