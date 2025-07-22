package com.stealthlynk.client.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stealthlynk.client.android.R
import com.stealthlynk.client.android.data.model.Server
import com.stealthlynk.client.android.ui.components.AppHeader
import com.stealthlynk.client.android.ui.components.GradientBackground
import com.stealthlynk.client.android.ui.tabs.ConnectionTab
import com.stealthlynk.client.android.ui.tabs.InfoTab
import com.stealthlynk.client.android.ui.tabs.ServersTab

@Composable
fun StealthLynkApp(
    isConnected: Boolean,
    connectionState: String,
    vpnPermissionGranted: Boolean,
    servers: List<Server>,
    activeServerId: String?,
    currentIp: String,
    onRequestVpnPermission: () -> Unit,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onAddServer: (String) -> Unit,
    onDeleteServer: (String) -> Unit,
    onSetActiveServer: (String) -> Unit,
    onToggleSmartConnect: (Boolean) -> Unit
) {
    // Tab selection state
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    // VPN permission dialog state
    var showVpnPermissionDialog by remember { mutableStateOf(!vpnPermissionGranted) }
    
    // Show VPN permission dialog if needed
    if (showVpnPermissionDialog) {
        AlertDialog(
            onDismissRequest = { /* Cannot dismiss */ },
            title = { Text(text = stringResource(R.string.app_name)) },
            text = { Text(stringResource(R.string.vpn_permission_required)) },
            confirmButton = {
                TextButton(onClick = { 
                    showVpnPermissionDialog = false
                    onRequestVpnPermission() 
                }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
    
    // Main UI with gradient background
    GradientBackground() {
        Column(modifier = Modifier.fillMaxSize()) {
            // App header with tabs
            AppHeader(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
            
            // Content based on selected tab
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.ui.graphics.Color.Transparent
            ) {
                when (selectedTabIndex) {
                    0 -> ConnectionTab(
                        isConnected = isConnected,
                        connectionState = connectionState,
                        server = servers.find { it.id == activeServerId },
                        currentIp = currentIp,
                        onConnect = {
                            if (vpnPermissionGranted) {
                                onConnect()
                            } else {
                                showVpnPermissionDialog = true
                            }
                        },
                        onDisconnect = onDisconnect,
                        onToggleSmartConnect = onToggleSmartConnect,
                        modifier = Modifier.padding(16.dp)
                    )
                    1 -> ServersTab(
                        servers = servers,
                        activeServerId = activeServerId,
                        onAddServer = onAddServer,
                        onDeleteServer = onDeleteServer,
                        onServerSelected = onSetActiveServer,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    2 -> InfoTab(
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
