package com.snatik.matches.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.snatik.matches.R
import com.snatik.matches.common.Music
import com.snatik.matches.common.Shared
import com.snatik.matches.events.ui.BackGameEvent
import com.snatik.matches.events.ui.NextGameEvent
import com.snatik.matches.model.GameState
import com.snatik.matches.utils.Clock
import com.snatik.matches.utils.Clock.OnTimerCount
import com.snatik.matches.utils.FontLoader

class PopupWonView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : RelativeLayout(context, attrs) {
    private val mTime: TextView?
    private val mScore: TextView?
    private val mStar1: ImageView?
    private val mStar2: ImageView?
    private val mStar3: ImageView?
    private val mNextButton: ImageView?
    private val mBackButton: ImageView?
    private val mHandler: Handler?
    fun setGameState(gameState: GameState?) {
        val min = gameState!!.remainedSeconds / 60
        val sec = gameState.remainedSeconds - min * 60
        mTime!!.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec))
        mScore!!.setText("" + 0)
        mHandler!!.postDelayed(Runnable {
            animateScoreAndTime(gameState.remainedSeconds, gameState.achievedScore)
            animateStars(gameState.achievedStars)
        }, 500)
    }

    private fun animateStars(start: Int) {
        when (start) {
            0 -> {
                mStar1?.setVisibility(View.GONE)
                mStar2?.setVisibility(View.GONE)
                mStar3?.setVisibility(View.GONE)
            }
            1 -> {
                mStar2?.setVisibility(View.GONE)
                mStar3?.setVisibility(View.GONE)
                mStar1?.setAlpha(0f)
                animateStar(mStar1, 0)
            }
            2 -> {
                mStar3?.setVisibility(View.GONE)
                mStar1?.setVisibility(View.VISIBLE)
                mStar1?.setAlpha(0f)
                animateStar(mStar1, 0)
                mStar2?.setVisibility(View.VISIBLE)
                mStar2?.setAlpha(0f)
                animateStar(mStar2, 600)
            }
            3 -> {
                mStar1?.setVisibility(View.VISIBLE)
                mStar1?.setAlpha(0f)
                animateStar(mStar1, 0)
                mStar2?.setVisibility(View.VISIBLE)
                mStar2?.setAlpha(0f)
                animateStar(mStar2, 600)
                mStar3?.setVisibility(View.VISIBLE)
                mStar3?.setAlpha(0f)
                animateStar(mStar3, 1200)
            }
            else -> {
            }
        }
    }

    private fun animateStar(view: View?, delay: Int) {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        alpha.duration = 100
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alpha, scaleX, scaleY)
        animatorSet.interpolator = BounceInterpolator()
        animatorSet.startDelay = delay.toLong()
        animatorSet.duration = 600
        view!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        animatorSet.start()
        mHandler!!.postDelayed(Runnable { Music.showStar() }, delay.toLong())
    }

    private fun animateScoreAndTime(remainedSeconds: Int, achievedScore: Int) {
        val totalAnimation = 1200
        Clock.Companion.getInstance()!!.startTimer(totalAnimation.toLong(), 35, object : OnTimerCount {
            override fun onTick(millisUntilFinished: Long) {
                val factor = millisUntilFinished / (totalAnimation * 1f) // 0.1
                val scoreToShow = achievedScore - (achievedScore * factor).toInt()
                val timeToShow = (remainedSeconds * factor).toInt()
                val min = timeToShow / 60
                val sec = timeToShow - min * 60
                mTime!!.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec))
                mScore!!.setText("" + scoreToShow)
            }

            override fun onFinish() {
                mTime!!.setText(" " + String.format("%02d", 0) + ":" + String.format("%02d", 0))
                mScore!!.setText("" + achievedScore)
            }
        })
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.popup_won_view, this, true)
        mTime = findViewById<View?>(R.id.time_bar_text) as TextView
        mScore = findViewById<View?>(R.id.score_bar_text) as TextView
        mStar1 = findViewById<View?>(R.id.star_1) as ImageView
        mStar2 = findViewById<View?>(R.id.star_2) as ImageView
        mStar3 = findViewById<View?>(R.id.star_3) as ImageView
        mBackButton = findViewById<View?>(R.id.button_back) as ImageView
        mNextButton = findViewById<View?>(R.id.button_next) as ImageView
        FontLoader.setTypeface(context, arrayOf(mTime, mScore), FontLoader.Font.GROBOLD)
        setBackgroundResource(R.drawable.level_complete)
        mHandler = Handler()
        mBackButton.setOnClickListener(OnClickListener { Shared.eventBus!!.notify(BackGameEvent()) })
        mNextButton.setOnClickListener(OnClickListener { Shared.eventBus!!.notify(NextGameEvent()) })
    }
}