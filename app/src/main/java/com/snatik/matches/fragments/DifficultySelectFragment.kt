package com.snatik.matches.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.TextView
import com.snatik.matches.R
import com.snatik.matches.common.Memory
import com.snatik.matches.common.Shared
import com.snatik.matches.events.ui.DifficultySelectedEvent
import com.snatik.matches.ui.DifficultyView

class DifficultySelectFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(Shared.context).inflate(R.layout.difficulty_select_fragment, container, false)
        val theme = Shared.engine!!.getSelectedTheme()
        val difficulty1 = view.findViewById<View?>(R.id.select_difficulty_1) as DifficultyView
        difficulty1.setDifficulty(1, Memory.getHighStars(theme!!.id, 1))
        setOnClick(difficulty1, 1)
        val difficulty2 = view.findViewById<View?>(R.id.select_difficulty_2) as DifficultyView
        difficulty2.setDifficulty(2, Memory.getHighStars(theme.id, 2))
        setOnClick(difficulty2, 2)
        val difficulty3 = view.findViewById<View?>(R.id.select_difficulty_3) as DifficultyView
        difficulty3.setDifficulty(3, Memory.getHighStars(theme.id, 3))
        setOnClick(difficulty3, 3)
        val difficulty4 = view.findViewById<View?>(R.id.select_difficulty_4) as DifficultyView
        difficulty4.setDifficulty(4, Memory.getHighStars(theme.id, 4))
        setOnClick(difficulty4, 4)
        val difficulty5 = view.findViewById<View?>(R.id.select_difficulty_5) as DifficultyView
        difficulty5.setDifficulty(5, Memory.getHighStars(theme.id, 5))
        setOnClick(difficulty5, 5)
        val difficulty6 = view.findViewById<View?>(R.id.select_difficulty_6) as DifficultyView
        difficulty6.setDifficulty(6, Memory.getHighStars(theme.id, 6))
        setOnClick(difficulty6, 6)
        animate(difficulty1, difficulty2, difficulty3, difficulty4, difficulty5, difficulty6)
        val type = Typeface.createFromAsset(Shared.context!!.assets, "fonts/grobold.ttf")
        val text1 = view.findViewById<View?>(R.id.time_difficulty_1) as TextView
        text1.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        text1.typeface = type
        text1.text = getBestTimeForStage(theme!!.id, 1)
        val text2 = view.findViewById<View?>(R.id.time_difficulty_2) as TextView
        text2.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        text2.typeface = type
        text2.text = getBestTimeForStage(theme.id, 2)
        val text3 = view.findViewById<View?>(R.id.time_difficulty_3) as TextView
        text3.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        text3.typeface = type
        text3.text = getBestTimeForStage(theme.id, 3)
        val text4 = view.findViewById<View?>(R.id.time_difficulty_4) as TextView
        text4.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        text4.typeface = type
        text4.text = getBestTimeForStage(theme.id, 4)
        val text5 = view.findViewById<View?>(R.id.time_difficulty_5) as TextView
        text5.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        text5.typeface = type
        text5.text = getBestTimeForStage(theme.id, 5)
        val text6 = view.findViewById<View?>(R.id.time_difficulty_6) as TextView
        text6.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        text6.typeface = type
        text6.text = getBestTimeForStage(theme.id, 6)
        return view
    }

    private fun getBestTimeForStage(theme: Int, difficulty: Int): String? {
        val bestTime = Memory.getBestTime(theme, difficulty)
        return if (bestTime != -1) {
            val minutes = bestTime % 3600 / 60
            val seconds = bestTime % 60
            String.format("BEST : %02d:%02d", minutes, seconds)
        } else {
            "BEST : -"
        }
    }

    private fun animate(vararg view: View?) {
        val animatorSet = AnimatorSet()
        val builder = animatorSet.play(AnimatorSet())
        for (i in 0 until view.size) {
            val scaleX = ObjectAnimator.ofFloat(view[i], "scaleX", 0.8f, 1f)
            val scaleY = ObjectAnimator.ofFloat(view[i], "scaleY", 0.8f, 1f)
            builder.with(scaleX).with(scaleY)
        }
        animatorSet.duration = 500
        animatorSet.interpolator = BounceInterpolator()
        animatorSet.start()
    }

    private fun setOnClick(view: View?, difficulty: Int) {
        view!!.setOnClickListener(View.OnClickListener { Shared.eventBus!!.notify(DifficultySelectedEvent(difficulty)) })
    }
}