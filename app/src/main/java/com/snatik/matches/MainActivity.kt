package com.snatik.matches

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ImageView
import com.snatik.matches.common.Shared
import com.snatik.matches.engine.Engine
import com.snatik.matches.engine.ScreenController
import com.snatik.matches.engine.ScreenController.Screen
import com.snatik.matches.events.EventBus
import com.snatik.matches.events.ui.BackGameEvent
import com.snatik.matches.ui.PopupManager
import com.snatik.matches.utils.Utils


class MainActivity : FragmentActivity() {
    private var mBackgroundImage: ImageView? = null
    private var currentApiVersion = 0
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        currentApiVersion = Build.VERSION.SDK_INT

        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // This work only for android 4.4+

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            val decorView = window.decorView
            decorView
                    .setOnSystemUiVisibilityChangeListener { visibility ->
                        if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                            decorView.systemUiVisibility = flags
                        }
                    }
        }
        super.onCreate(savedInstanceState)
        Shared.context = applicationContext
        Shared.engine = Engine.Companion.getInstance()
        Shared.eventBus = EventBus.Companion.getInstance()
        setContentView(R.layout.activity_main)
        mBackgroundImage = findViewById<View?>(R.id.background_image) as ImageView
        Shared.activity = this
        Shared.engine!!.start()
        Shared.engine!!.setBackgroundImageView(mBackgroundImage)

        // set background
        setBackgroundImage()

        // set menu
        ScreenController.Companion.getInstance()!!.openScreen(Screen.MENU)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
    override fun onDestroy() {
        Shared.engine!!.stop()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (PopupManager.isShown()) {
            PopupManager.closePopup()
            if (ScreenController.Companion.getLastScreen() == Screen.GAME) {
                Shared.eventBus!!.notify(BackGameEvent())
            }
        } else if (ScreenController.Companion.getInstance()!!.onBack()) {
            super.onBackPressed()
        }
    }

    private fun setBackgroundImage() {
        var bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight())
        bitmap = Utils.crop(bitmap, Utils.screenHeight(), Utils.screenWidth())
        bitmap = Utils.downscaleBitmap(bitmap, 2)
        mBackgroundImage!!.setImageBitmap(bitmap)
    }
}