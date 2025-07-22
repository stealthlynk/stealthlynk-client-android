package com.stealthlynk.client.android

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.stealthlynk.client.android.data.repository.ServerRepository
import com.stealthlynk.client.android.data.repository.XrayConfigRepository
import com.stealthlynk.client.android.util.SampleDataImporter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

// DataStore is already defined in DataStore.kt

class StealthLynkApplication : Application() {
    // Application scope for coroutines
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    
    // Repositories for server management and xray configuration
    val serverRepository by lazy { ServerRepository(applicationContext) }
    val xrayConfigRepository by lazy { XrayConfigRepository(applicationContext) }
    private val sampleDataImporter by lazy { SampleDataImporter(applicationContext) }

    companion object {
        // Application singleton instance
        lateinit var instance: StealthLynkApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        Timber.i("StealthLynk Android application starting...")
        
        // Import sample data for first run
        applicationScope.launch {
            sampleDataImporter.importSampleDataIfNeeded(serverRepository)
        }
    }
}
