package com.stealthlynk.client.android

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Create a DataStore instance at the app level
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "stealthlynk_settings")
