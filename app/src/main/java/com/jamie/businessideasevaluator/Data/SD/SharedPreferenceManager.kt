package com.jamie.businessideasevaluator.Data.SD

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {
    private val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    fun hasCompletedTutorial(): Boolean {
        return prefs.getBoolean("hasCompletedTutorial", false)
    }

    fun setTutorialCompleted() {
        prefs.edit().putBoolean("hasCompletedTutorial", true).apply()
    }
}