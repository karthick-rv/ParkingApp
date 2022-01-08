package com.example.parkingapp.feature_parking.presentation.system_create

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.parkingapp.SystemConfigStore
import com.example.parkingapp.feature_parking.domain.model.ParkingLotConfig
import com.example.parkingapp.feature_parking.domain.util.SystemConfigStoreSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class SystemConfigManager(val context: Context) {

    companion object {
        private const val SYSTEM_CONFIG_STORE_FILE_NAME = "user_store.pb"
    }

    private val Context.systemConfigDataStore: DataStore<SystemConfigStore> by dataStore(
        fileName = SYSTEM_CONFIG_STORE_FILE_NAME,
        serializer = SystemConfigStoreSerializer
    )

    suspend fun getSystemConfig(): Flow<SystemConfigStore> {
        return context.systemConfigDataStore.data.catch { exception ->
            if(exception is IOException){
                emit(SystemConfigStore.getDefaultInstance())
            } else{
                throw exception
            }
        }.map{ builder ->
            builder
        }
    }

    suspend fun storeSystemConfig(systemConfig: ParkingLotConfig) {
        context.systemConfigDataStore.updateData { store ->
            store.toBuilder()
                .setFloorCount(systemConfig.floorCount)
                .setParkingSpaceCount(systemConfig.parkingSpaceCount)
                .setSystemCreated(true)
                .build()
        }
    }

}