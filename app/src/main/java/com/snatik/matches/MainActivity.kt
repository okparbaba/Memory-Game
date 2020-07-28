package com.snatik.matches

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.WindowManager
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
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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