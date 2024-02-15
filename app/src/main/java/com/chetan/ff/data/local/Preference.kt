package com.chetan.orderdelivery.data.local


import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preference @Inject constructor(
    val context : Context
) {
    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val _audioIndexFlow = MutableSharedFlow<Int>(replay = 1)

    // Expose a read-only SharedFlow for external observation
    val audioIndexFlow = _audioIndexFlow.asSharedFlow()

    companion object {
        private const val PREFERENCE_NAME = "PREFERENCE_NAME"
        private const val USER_NAME = "USER_NAME"
        private const val IS_DARK_MODE = "IS_DARK_MODE"
        private const val TABLE_NAME = "TABLE_NAME"
        private const val GMAIL_PROFILE = "GMAIL_PROFILE"
        private const val NOTIFICATION = "NOTIFICATION"
        private const val GROUP_NAME = "GROUP_NAME"
        private const val AUDIO_INDEX = "AUDIO_INDEX"
        private const val IS_AUDIO_PLAYING = "IS_AUDIO_PLAYING"
    }

    var isDarkMode
        get() = mutableStateOf(sharedPreference.getBoolean(IS_DARK_MODE, false))
        set(value) {sharedPreference.edit().putBoolean(IS_DARK_MODE,value.value).apply()}
    var isNewNotification
        get() = sharedPreference.getBoolean(NOTIFICATION, false)
        set(value) {sharedPreference.edit().putBoolean(NOTIFICATION,value).apply()}

    var userName
        get() = sharedPreference.getString(USER_NAME,"")
        set(value) {
            sharedPreference.edit().putString(USER_NAME,value).apply()
        }

    var gmailProfile
        get() = sharedPreference.getString(GMAIL_PROFILE, "")
        set(value) {sharedPreference.edit().putString(GMAIL_PROFILE, value).apply()}
    var tableName
        get() = sharedPreference.getString(TABLE_NAME,"")
        set(value) {sharedPreference.edit().putString(TABLE_NAME,value).apply()}

    var groupName
        get() = sharedPreference.getString(GROUP_NAME,"")
        set(value) {sharedPreference.edit().putString(GROUP_NAME,value).apply()}

    var audioIndex : Int
        get() = sharedPreference.getInt(AUDIO_INDEX, 0)
        set(value) {sharedPreference.edit().putInt(AUDIO_INDEX, value).apply()
            _audioIndexFlow.tryEmit(value)
        }



}