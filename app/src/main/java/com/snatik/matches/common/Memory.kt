package com.snatik.matches.common

import android.content.Context

object Memory {
    private val SHARED_PREFERENCES_NAME: String? = "com.snatik.matches"
    private val highStartKey: String? = "theme_%d_difficulty_%d"
    private val bestTimeKey: String? = "themetime_%d_difficultytime_%d"
    fun save(theme: Int, difficulty: Int, stars: Int) {
        val highStars = getHighStars(theme, difficulty)
        if (stars > highStars) {
            val sharedPreferences = Shared.context?.getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE)
            val edit = sharedPreferences?.edit()
            val key = highStartKey?.let { String.format(it, theme, difficulty) }
            edit?.putInt(key, stars)?.apply()
        }
    }

    fun saveTime(theme: Int, difficulty: Int, passedSecs: Int) {
        val bestTime = getBestTime(theme, difficulty)
        if (passedSecs < bestTime || bestTime == -1) {
            val sharedPreferences = Shared.context?.getSharedPreferences(SHARED_PREFERENCES_NAME,
                    Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            val timeKey = bestTimeKey?.let { String.format(it, theme, difficulty) }
            editor?.putInt(timeKey, passedSecs)
            editor?.apply()
        }
    }

    fun getHighStars(theme: Int, difficulty: Int): Int {
        val sharedPreferences = Shared.context?.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE)
        val key = highStartKey?.let { String.format(it, theme, difficulty) }
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 0)
        }
        return 0
    }

    fun getBestTime(theme: Int, difficulty: Int): Int {
        val sharedPreferences = Shared.context?.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val key = bestTimeKey?.let { String.format(it, theme, difficulty) }
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, -1)
        }
        return 0
    }
}