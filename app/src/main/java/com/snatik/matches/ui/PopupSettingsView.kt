package com.snatik.matches.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.snatik.matches.R
import com.snatik.matches.common.Music
import com.snatik.matches.common.Shared
import com.snatik.matches.utils.FontLoader

class PopupSettingsView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val mSoundImage: ImageView?
    private val mSoundText: TextView?
    private fun setMusicButton() {
        if (Music.OFF) {
            mSoundText!!.setText("Sound OFF")
            mSoundImage!!.setImageResource(R.drawable.button_music_off)
        } else {
            mSoundText!!.setText("Sound ON")
            mSoundImage!!.setImageResource(R.drawable.button_music_on)
        }
    }

    init {
        orientation = VERTICAL
        setBackgroundResource(R.drawable.settings_popup)
        LayoutInflater.from(getContext()).inflate(R.layout.popup_settings_view, this, true)
        mSoundText = findViewById<View?>(R.id.sound_off_text) as TextView
        val rateView = findViewById<View?>(R.id.rate_text) as TextView
        FontLoader.setTypeface(context, arrayOf(mSoundText, rateView), FontLoader.Font.GROBOLD)
        mSoundImage = findViewById<View?>(R.id.sound_image) as ImageView
        val soundOff = findViewById<View?>(R.id.sound_off)
        soundOff.setOnClickListener {
            Music.OFF = !Music.OFF
            setMusicButton()
        }
        val rate = findViewById<View?>(R.id.rate)
        rate.setOnClickListener {
            val appPackageName = Shared.context!!.packageName
            try {
                Shared.activity!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: ActivityNotFoundException) {
                Shared.activity!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
        setMusicButton()
    }
}