package com.example.llm_enhancedlearningassistantapp.data

import android.content.Context
import android.content.SharedPreferences

class UserPrefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUsername(username: String) {
        prefs.edit().putString("username", username).apply()
    }

    fun getUsername(): String = prefs.getString("username", "Student") ?: "Student"

    fun saveInterests(interests: Set<String>) {
        prefs.edit().putStringSet("interests", interests).apply()
    }

    fun getInterests(): Set<String> = prefs.getStringSet("interests", emptySet()) ?: emptySet()

    fun clear() {
        prefs.edit().clear().apply()
    }
}
