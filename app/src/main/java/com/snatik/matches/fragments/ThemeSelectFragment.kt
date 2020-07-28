package com.snatik.matches.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.snatik.matches.R
import com.snatik.matches.common.Memory
import com.snatik.matches.common.Shared
import com.snatik.matches.events.ui.ThemeSelectedEvent
import com.snatik.matches.themes.Theme
import com.snatik.matches.themes.Themes
import java.util.*

class ThemeSelectFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(Shared.context).inflate(R.layout.theme_select_fragment, container, false)
        val animals = view.findViewById<View?>(R.id.theme_animals_container)
        val monsters = view.findViewById<View?>(R.id.theme_monsters_container)
        val emoji = view.findViewById<View?>(R.id.theme_emoji_container)
        val themeAnimals = Themes.createAnimalsTheme()
        setStars(animals.findViewById<View?>(R.id.theme_animals) as ImageView, themeAnimals, "animals")
        val themeMonsters = Themes.createMosterTheme()
        setStars(monsters.findViewById<View?>(R.id.theme_monsters) as ImageView, themeMonsters, "monsters")
        val themeEmoji = Themes.createEmojiTheme()
        setStars(emoji.findViewById<View?>(R.id.theme_emoji) as ImageView, themeEmoji, "emoji")
        animals.setOnClickListener { Shared.eventBus!!.notify(ThemeSelectedEvent(themeAnimals)) }
        monsters.setOnClickListener { Shared.eventBus!!.notify(ThemeSelectedEvent(themeMonsters)) }
        emoji.setOnClickListener { Shared.eventBus!!.notify(ThemeSelectedEvent(themeEmoji)) }
        /**
         * Imporove performance first!!!
         */
        animateShow(animals)
        animateShow(monsters)
        animateShow(emoji)
        return view
    }

    private fun animateShow(view: View?) {
        val animatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f)
        val animatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 300
        animatorSet.playTogether(animatorScaleX, animatorScaleY)
        animatorSet.interpolator = DecelerateInterpolator(2f)
        view!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        animatorSet.start()
    }

    private fun setStars(imageView: ImageView?, theme: Theme?, type: String?) {
        var sum = 0
        for (difficulty in 1..6) {
            sum += Memory.getHighStars(theme!!.id, difficulty)
        }
        val num = sum / 6
        if (num != 0) {
            val drawableResourceName = String.format(Locale.US, type + "_theme_star_%d", num)
            val drawableResourceId = Shared.context!!.resources.getIdentifier(drawableResourceName, "drawable", Shared.context!!.packageName)
            imageView!!.setImageResource(drawableResourceId)
        }
    }
}