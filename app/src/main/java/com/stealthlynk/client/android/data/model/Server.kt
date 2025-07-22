package com.stealthlynk.client.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Server(
    val id: String = UUID.randomUUID().toString(),
    val protocol: String = "vless",
    val name: String,
    val address: String,
    val port: Int,
    val countryCode: String = "",
    val countryName: String = "",
    val flag: String = "🌐",
    val rawConfig: VlessConfig,
    val addedAt: String
) : Parcelable

@Parcelize
data class VlessConfig(
    val id: String, // User ID
    val add: String, // Address/hostname
    val port: Int, // Port
    val type: String = "tcp", // Connection type
    val encryption: String = "none", // Encryption method
    val protocol: String = "vless", // Protocol identifier
    val ps: String, // Server name
    val net: String = "tcp", // Network type
    val tls: String = "none", // TLS setting
    val sni: String = "", // SNI value
    val fp: String = "chrome", // TLS fingerprint
    val path: String = "/", // Path value
    val peer: String = "", // Server name for TLS
    val flow: String = "", // Flow setting for XTLS Vision
    val pbk: String = "", // Public key for Reality
    val sid: String = "", // Short ID for Reality
    val spx: String = "/" // Spider X for Reality
) : Parcelable
