package com.example.weathers.data.source.local.store

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.example.weathers.Settings
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.jvm.internal.impl.protobuf.InvalidProtocolBufferException

object SettingsSerializer : Serializer<Settings> {
  override val defaultValue: Settings = Settings.getDefaultInstance()

  override suspend fun readFrom(input: InputStream): Settings {
    try {
      return Settings.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
      throw CorruptionException("Cannot read proto.", exception)
    }
  }

  override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
}

val Context.settingsDataStore: DataStore<Settings> by dataStore(fileName = "settings.pb", serializer = SettingsSerializer)