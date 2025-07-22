package com.stealthlynk.client.android.ui.tabs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stealthlynk.client.android.R

@Composable
fun InfoTab(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.about_stealthlynk),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.stealthlynk_description),
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = R.color.text_color),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        FeaturesCard()

        Spacer(modifier = Modifier.height(24.dp))

        OfficialLinks()
    }
}

@Composable
fun FeaturesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.secondary_bg)
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(colorResource(id = R.color.primary))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.with_stealthlynk_you_can),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_color)
                )

                val features = listOf(
                    stringResource(R.string.feature_launch_vpn),
                    stringResource(R.string.feature_connect_nodes),
                    stringResource(R.string.feature_monetize_connection),
                    stringResource(R.string.feature_protect_identity)
                )

                features.forEach {
                    FeatureItem(text = it)
                }
            }
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "•",
            color = colorResource(id = R.color.primary),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            color = colorResource(id = R.color.text_color),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun OfficialLinks() {
    val context = LocalContext.current

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.official_links),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinkIcon(icon = Icons.Default.Language, url = "https://stealthlynk.com")
            LinkIcon(icon = Icons.Default.Send, url = "https://t.me/stealthlynk")
                        LinkIcon(icon = Icons.Default.Send, url = "https://t.me/stealthlynk_channel")
            LinkIcon(icon = Icons.Default.Close, url = "https://x.com/stealthlynk")
        }
    }
}

@Composable
fun LinkIcon(icon: ImageVector, url: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .size(50.dp)
            .clickable { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) },
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.secondary_bg)
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(id = R.color.primary),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
