package com.snatik.matches.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.snatik.matches.R
import com.snatik.matches.common.Shared
import java.util.*

class DifficultyView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) : LinearLayout(context, attrs) {
    private val mTitle: ImageView?
    fun setDifficulty(difficulty: Int, stars: Int) {
        val titleResource = String.format(Locale.US, "button_difficulty_%d_star_%d", difficulty, stars)
        val drawableResourceId = Shared.context!!.resources.getIdentifier(titleResource, "drawable", Shared.context!!.packageName)
        mTitle!!.setImageResource(drawableResourceId)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.difficult_view, this, true)
        orientation = VERTICAL
        mTitle = findViewById<View?>(R.id.title) as ImageView
    }
}