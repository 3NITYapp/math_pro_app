package ru.ivos.mathproapp.data.local

import android.content.Context
import androidx.preference.PreferenceManager

class PreferencesRepo(context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun getHighScore() : Long = prefs.getLong("high_score", 0)


    fun setHighScore(score: Long) {
        prefs.edit().putLong("high_score", score).apply()
    }
}