package tkuik.alexkarav.tkuikstudent.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KeyStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("timetableTokens")

        private val _AUTH_TOKEN = stringPreferencesKey("token")
        private val _USER_GROUP_ID = intPreferencesKey("group_id")
        private val _USER_LOGIN = stringPreferencesKey("login")
        private val _USER_GROUP_NAME = stringPreferencesKey("group_name")
        private val _CURRENT_LESSON_KEY = stringPreferencesKey("current_pair")
        private val _CURRENT_CABINET_KEY = stringPreferencesKey("current_cabinet")
    }

    val getAuthToken: Flow<String> = context.dataStore.data.map { settings ->
        settings[_AUTH_TOKEN] ?: ""
    }

    val getGroupId: Flow<Int> = context.dataStore.data.map { settings ->
        settings[_USER_GROUP_ID] ?: 0
    }

    suspend fun setAuthToken(token: String) {
        context.dataStore.edit { settings ->
            settings[_AUTH_TOKEN] = token
        }
    }

    suspend fun setUserGroup(id: Int) {
        context.dataStore.edit { settings ->
            settings[_USER_GROUP_ID] = id
        }
    }

    val getUserLogin: Flow<String> = context.dataStore.data.map { settings ->
        settings[_USER_LOGIN] ?: ""
    }

    suspend fun setUserToken(login: String) {
        context.dataStore.edit { settings ->
            settings[_USER_LOGIN] = login
        }
    }

    val getUserGroupName: Flow<String> = context.dataStore.data.map { settings ->
        settings[_USER_GROUP_NAME] ?: ""
    }

    suspend fun setUserGroupName(name: String) {
        context.dataStore.edit { settings ->
            settings[_USER_GROUP_NAME] = name
        }
    }

    suspend fun setCurrentPairInfo(lessonName: String, cabinet: String) {
        context.dataStore.edit { settings ->
            settings[_CURRENT_CABINET_KEY] = cabinet
            settings[_CURRENT_LESSON_KEY] = cabinet
        }
    }
}