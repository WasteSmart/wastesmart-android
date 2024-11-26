package com.frxcl.wastesmart.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val setupKey = booleanPreferencesKey("setup")
    private val usernameKey = stringPreferencesKey("user_name")
    private val themeKey = booleanPreferencesKey("theme_setting")

    suspend fun saveSetupState(isSetupDone: Boolean) {
        dataStore.edit { preferences ->
            preferences[setupKey] = isSetupDone
        }
    }

    fun getSetupState(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[setupKey] ?: false
        }
    }

    suspend fun saveUserName (username: String) {
        dataStore.edit { preferences ->
            preferences[usernameKey] = username
        }
    }

    fun getUserName(): Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[usernameKey]
            }
    }


    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}