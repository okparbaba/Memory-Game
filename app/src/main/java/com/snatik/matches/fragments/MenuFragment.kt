package com.snatik.matches.fragments

import android.animation.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.Toast
import com.snatik.matches.R
import com.snatik.matches.common.Music
import com.snatik.matches.common.Shared
import com.snatik.matches.events.ui.StartEvent
import com.snatik.matches.ui.PopupManager
import com.snatik.matches.utils.Utils

class MenuFragment : Fragment() {
    private var mTitle: ImageView? = null
    private var mStartGameButton: ImageView? = null
    private var mStartButtonLights: ImageView? = null
    private var mTooltip: ImageView? = null
    private var mSettingsGameButton: ImageView? = null
    private var mGooglePlayGameButton: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.menu_fragment, container, false)
        mTitle = view.findViewById<View?>(R.id.title) as ImageView
        mStartGameButton = view.findViewById<View?>(R.id.start_game_button) as ImageView
        mSettingsGameButton = view.findViewById<View?>(R.id.settings_game_button) as ImageView
        mSettingsGameButton!!.setSoundEffectsEnabled(false)
        mSettingsGameButton!!.setOnClickListener(View.OnClickListener { PopupManager.showPopupSettings() })
        mGooglePlayGameButton = view.findViewById<View?>(R.id.google_play_button) as ImageView
        mGooglePlayGameButton!!.setOnClickListener(View.OnClickListener { Toast.makeText(activity, "Leaderboards will be available in the next game updates", Toast.LENGTH_LONG).show() })
        mStartButtonLights = view.findViewById<View?>(R.id.start_game_button_lights) as ImageView
        mTooltip = view.findViewById<View?>(R.id.tooltip) as ImageView
        mStartGameButton!!.setOnClickListener(View.OnClickListener { // animate title from place and navigation buttons from place
            animateAllAssetsOff(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    Shared.eventBus!!.notify(StartEvent())
                }
            })
        })
        startLightsAnimation()
        startTootipAnimation()

        // play background music
        Music.playBackgroundMusic()
        return view
    }

    @SuppressLint("ObjectAnimatorBinding")
    protected fun animateAllAssetsOff(adapter: AnimatorListenerAdapter?) {
        // title
        // 120dp + 50dp + buffer(30dp)
        val titleAnimator = ObjectAnimator.ofFloat(mTitle, "translationY", Utils.px(-200).toFloat())
        titleAnimator.interpolator = AccelerateInterpolator(2f)
        titleAnimator.duration = 300

        // lights
        val lightsAnimatorX = ObjectAnimator.ofFloat(mStartButtonLights, "scaleX", 0f)
        val lightsAnimatorY = ObjectAnimator.ofFloat(mStartButtonLights, "scaleY", 0f)

        // tooltip
        val tooltipAnimator = ObjectAnimator.ofFloat(mTooltip, "alpha", 0f)
        tooltipAnimator.duration = 100

        // settings button
        val settingsAnimator = ObjectAnimator.ofFloat(mSettingsGameButton, "translationY", Utils.px(120).toFloat())
        settingsAnimator.interpolator = AccelerateInterpolator(2f)
        settingsAnimator.duration = 300

        // google play button
        val googlePlayAnimator = ObjectAnimator.ofFloat(mGooglePlayGameButton, "translationY", Utils.px(120).toFloat())
        googlePlayAnimator.interpolator = AccelerateInterpolator(2f)
        googlePlayAnimator.duration = 300

        // start button
        val startButtonAnimator = ObjectAnimator.ofFloat(mStartGameButton, "translationY", Utils.px(130).toFloat())
        startButtonAnimator.interpolator = AccelerateInterpolator(2f)
        startButtonAnimator.duration = 300
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(titleAnimator, lightsAnimatorX, lightsAnimatorY, tooltipAnimator, settingsAnimator, googlePlayAnimator, startButtonAnimator)
        animatorSet.addListener(adapter)
        animatorSet.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun startTootipAnimation() {
        val scaleY = ObjectAnimator.ofFloat(mTooltip, "scaleY", 0.8f)
        scaleY.duration = 200
        val scaleYBack = ObjectAnimator.ofFloat(mTooltip, "scaleY", 1f)
        scaleYBack.duration = 500
        scaleYBack.interpolator = BounceInterpolator()
        val animatorSet = AnimatorSet()
        animatorSet.startDelay = 1000
        animatorSet.playSequentially(scaleY, scaleYBack)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                animatorSet.startDelay = 2000
                animatorSet.start()
            }
        })
        mTooltip!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        animatorSet.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun startLightsAnimation() {
        val animator = ObjectAnimator.ofFloat(mStartButtonLights, "rotation", 0f, 360f)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 6000
        animator.repeatCount = ValueAnimator.INFINITE
        mStartButtonLights!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        animator.start()
    }
}