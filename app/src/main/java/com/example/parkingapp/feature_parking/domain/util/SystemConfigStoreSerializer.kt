package com.example.parkingapp.feature_parking.domain.util

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.parkingapp.SystemConfigStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SystemConfigStoreSerializer : Serializer<SystemConfigStore> {
    override val defaultValue: SystemConfigStore = SystemConfigStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SystemConfigStore {
        try {
            return SystemConfigStore.parseFrom(input)
        }catch (ex: InvalidProtocolBufferException){
            throw CorruptionException("Cannot read proto.", ex)
        }
    }

    override suspend fun writeTo(t: SystemConfigStore, output: OutputStream) {
       t.writeTo(output)
    }
}