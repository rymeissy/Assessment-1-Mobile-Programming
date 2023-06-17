package org.d3if3083.assessment2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val RESEP_PREFERENCES = "preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = RESEP_PREFERENCES
)

class SettingDataStore(prefDataStore: DataStore<Preferences>) {

    private val IS_FIRST_TIME = booleanPreferencesKey("is_linear_layout")

    val isFirstTime: Flow<Boolean> = prefDataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[IS_FIRST_TIME] ?: true }

    suspend fun saveFirstTime(isFirstTime: Boolean, context: Context) {
        context.dataStore.edit { it[IS_FIRST_TIME] = isFirstTime }
    }
}