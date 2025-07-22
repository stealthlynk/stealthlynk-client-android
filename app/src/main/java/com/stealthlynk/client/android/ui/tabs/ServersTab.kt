package com.stealthlynk.client.android.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.stealthlynk.client.android.R
import com.stealthlynk.client.android.data.model.Server
import com.stealthlynk.client.android.ui.components.QrScannerDialog

@Composable
fun ServersTab(
    servers: List<Server>,
    activeServerId: String?,
    onAddServer: (String) -> Unit,
    onDeleteServer: (String) -> Unit,
    onServerSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var vlessUrl by remember { mutableStateOf("") }
    var showQrScannerDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = vlessUrl,
            onValueChange = { vlessUrl = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.vless_url_hint)) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(id = R.color.secondary_bg),
                unfocusedContainerColor = colorResource(id = R.color.secondary_bg),
                disabledContainerColor = colorResource(id = R.color.secondary_bg),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { if (vlessUrl.isNotBlank()) onAddServer(vlessUrl) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.primary))
            ) {
                Text(stringResource(R.string.add_server))
            }
            Button(
                onClick = { showQrScannerDialog = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.secondary_bg))
            ) {
                Text(stringResource(R.string.scan_qr))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (servers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.secondary_bg), RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_servers_added),
                    color = colorResource(id = R.color.text_color)
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(servers) { server ->
                    ServerItem(
                        server = server,
                        isActive = server.id == activeServerId,
                        onServerSelected = { onServerSelected(server.id) },
                        onDeleteServer = { onDeleteServer(server.id) }
                    )
                }
            }
        }
    }

    if (showQrScannerDialog) {
        QrScannerDialog(
            onDismiss = { showQrScannerDialog = false },
            onQrCodeScanned = { code ->
                onAddServer(code)
                showQrScannerDialog = false
            }
        )
    }
}

@Composable
fun ServerItem(
    server: Server,
    isActive: Boolean,
    onServerSelected: () -> Unit,
    onDeleteServer: () -> Unit
) {
    val borderColor = if (isActive) colorResource(id = R.color.primary) else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onServerSelected),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.secondary_bg))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = server.flag, style = MaterialTheme.typography.headlineMedium)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = server.name,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_color),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${server.address}:${server.port}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (isActive) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(colorResource(id = R.color.success_color), CircleShape)
                )
            }

            IconButton(onClick = onDeleteServer, modifier = Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.delete_server),
                    tint = colorResource(id = R.color.error_color)
                )
            }
        }
    }
}
