package com.plantguide.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("plant_guide_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun saveLoginState(loggedIn: Boolean, email: String = "") {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, loggedIn)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    fun getUserEmail(): String = prefs.getString(KEY_USER_EMAIL, "") ?: ""

    fun clearLoginState() {
        prefs.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_USER_EMAIL)
            .apply()
    }
}
