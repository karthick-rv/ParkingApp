package com.example.parkingapp.feature_parking.presentation.system_create

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SystemConfigManager(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    companion object {
        val SYSTEM_CREATED = booleanPreferencesKey("system_config")
        val SYSTEM_FLOOR_COUNT = intPreferencesKey("system_floor_count")
        val SYSTEM_PARKING_SPACE_COUNT = intPreferencesKey("system_parking_space_count")
    }

    val systemCreatedFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SYSTEM_CREATED] ?: false
        }

    val systemFloorCountFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[SYSTEM_FLOOR_COUNT] ?: 0
        }

    val systemParkingSpaceCountFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[SYSTEM_PARKING_SPACE_COUNT] ?: 0
        }


    suspend fun storeSystemCreation(floorCount: Int, parkingSpaceCount: Int){
        context.dataStore.edit { settings ->
            settings[SYSTEM_CREATED] = true
            settings[SYSTEM_FLOOR_COUNT] = floorCount
            settings[SYSTEM_PARKING_SPACE_COUNT] = parkingSpaceCount
        }
    }
}