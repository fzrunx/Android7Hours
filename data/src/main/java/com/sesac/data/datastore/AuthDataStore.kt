package com.sesac.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sesac.domain.model.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
    private val moshi: Moshi
) {

    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_INFO = stringPreferencesKey("user_info")
    }

    @OptIn(ExperimentalStdlibApi::class)
    private val userAdapter = moshi.adapter<User>()

    val accessToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN]
        }

    val refreshToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.REFRESH_TOKEN]
        }

    val user: Flow<User?> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_INFO]?.let { jsonString ->
                Log.d("AuthDataStore", "Reading user JSON: $jsonString")
                userAdapter.fromJson(jsonString)
            }
        }

    suspend fun saveSession(accessToken: String, refreshToken: String, user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
            val userJson = userAdapter.toJson(user)
            Log.d("AuthDataStore", "Saving session user JSON: $userJson")
            preferences[PreferencesKeys.USER_INFO] = userJson
        }
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            val userJson = userAdapter.toJson(user)
            Log.d("AuthDataStore", "Saving user JSON: $userJson")
            preferences[PreferencesKeys.USER_INFO] = userJson
        }
    }
    
    suspend fun saveTokens(accessToken: String, refreshToken: String?) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            refreshToken?.let {
                preferences[PreferencesKeys.REFRESH_TOKEN] = it
            }
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.ACCESS_TOKEN)
            preferences.remove(PreferencesKeys.REFRESH_TOKEN)
            preferences.remove(PreferencesKeys.USER_INFO)
        }
    }
}
