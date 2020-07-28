package com.snatik.matches

import android.app.Application
import com.snatik.matches.utils.FontLoader

class GameApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FontLoader.loadFonts(this)
    }
}