package com.snatik.matches.themes

import android.graphics.Bitmap
import com.snatik.matches.common.Shared
import com.snatik.matches.utils.Utils
import java.util.*

object Themes {
    var URI_DRAWABLE: String? = "drawable://"
    fun createAnimalsTheme(): Theme? {
        val theme = Theme()
        theme.id = 1
        theme.name = "Animals"
        theme.backgroundImageUrl = URI_DRAWABLE + "back_animals"
        theme.tileImageUrls = ArrayList()
        // 40 drawables
        for (i in 1..28) {
            theme.tileImageUrls!!.add(URI_DRAWABLE + String.format("animals_%d", i))
        }
        return theme
    }

    fun createMosterTheme(): Theme? {
        val theme = Theme()
        theme.id = 2
        theme.name = "Mosters"
        theme.backgroundImageUrl = URI_DRAWABLE + "back_horror"
        theme.tileImageUrls = ArrayList()
        // 40 drawables
        for (i in 1..40) {
            theme.tileImageUrls!!.add(URI_DRAWABLE + String.format("mosters_%d", i))
        }
        return theme
    }

    fun createEmojiTheme(): Theme? {
        val theme = Theme()
        theme.id = 3
        theme.name = "Emoji"
        theme.backgroundImageUrl = URI_DRAWABLE + "background"
        theme.tileImageUrls = ArrayList()
        // 40 drawables
        for (i in 1..40) {
            theme.tileImageUrls!!.add(URI_DRAWABLE + String.format("emoji_%d", i))
        }
        return theme
    }

    fun getBackgroundImage(theme: Theme?): Bitmap? {
        val drawableResourceName = theme!!.backgroundImageUrl!!.substring(URI_DRAWABLE!!.length)
        val drawableResourceId = Shared.context!!.resources.getIdentifier(drawableResourceName, "drawable", Shared.context!!.packageName)
        return Utils.scaleDown(drawableResourceId, Utils.screenWidth(), Utils.screenHeight())
    }
}