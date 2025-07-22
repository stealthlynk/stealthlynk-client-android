package com.stealthlynk.client.android.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stealthlynk.client.android.R

@Composable
fun AppHeader(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // App title
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorResource(id = R.color.primary))) {
                    append("Stealth")
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                    append("Lynk")
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )
        
        // Tab row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            // Connection tab
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { onTabSelected(0) },
                text = {
                    Text(
                        text = stringResource(R.string.connection),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
            
            // Servers tab
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { onTabSelected(1) },
                text = {
                    Text(
                        text = stringResource(R.string.servers),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
            
            // Info tab
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { onTabSelected(2) },
                text = {
                    Text(
                        text = stringResource(R.string.info),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}
