package com.stealthlynk.client.android.ui.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stealthlynk.client.android.R
import com.stealthlynk.client.android.data.model.Server

@Composable
fun ConnectionTab(
    isConnected: Boolean,
    connectionState: String,
    server: Server?,
    currentIp: String,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit,
    onToggleSmartConnect: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
        var smartConnectEnabled by remember { mutableStateOf(true) }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Server Display
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(
                text = "Server",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = server?.let { "${it.address}:${it.port}" } ?: stringResource(R.string.no_server_selected),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Connection Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(200.dp)
        ) {
            val isConnecting = connectionState.equals("CONNECTING", ignoreCase = true)
            val isDisconnecting = connectionState.equals("DISCONNECTING", ignoreCase = true)

            val (buttonText, ringColor) = when {
                isConnecting -> stringResource(R.string.connecting) to colorResource(id = R.color.warning_color)
                isDisconnecting -> stringResource(R.string.disconnecting) to colorResource(id = R.color.error_color)
                isConnected -> stringResource(R.string.disconnect) to colorResource(id = R.color.success_color)
                else -> stringResource(R.string.connect) to MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }

            // The button now has a persistent border whose color changes with the state.
            Button(
                onClick = { if (isConnected) onDisconnect() else onConnect() },
                modifier = Modifier.size(180.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(6.dp, ringColor), // Always show border with the state color
                enabled = server != null || isConnected
            ) {
                // Use a smaller text style for longer strings to prevent wrapping
                val textStyle = if (buttonText.length > 10) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.titleLarge
                }

                Text(
                    text = buttonText,
                    style = textStyle,
                    fontWeight = FontWeight.Bold,
                    color = ringColor,
                    textAlign = TextAlign.Center
                )
            }
        }



        // Bottom Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Smart Connect Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.smart_connect_label),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.width(16.dp))
                Switch(
                    checked = smartConnectEnabled,
                    onCheckedChange = {
                        smartConnectEnabled = it
                        onToggleSmartConnect(it)
                    }
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.fastest_server_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Info Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.current_location),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                                    text = if (isConnected) server?.countryName ?: "Unknown" else "Not Connected",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isConnected) "00:00:00" else "--:--:--", // Placeholder for uptime
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
