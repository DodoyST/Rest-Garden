package com.example.restgarden.di.module

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.restgarden.util.SessionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule(private val application: Application) {
  
  @Provides
  @Singleton
  fun provideSharedPreference(): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    return EncryptedSharedPreferences.create(
      "SharedPref",
      masterKeyAlias,
      application,
      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
  }
  
  @Provides
  @Singleton
  fun provideSessionManager(sharedPreferences: SharedPreferences): SessionManager =
    SessionManager(sharedPreferences)
}
