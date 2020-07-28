package com.snatik.matches.model

import android.graphics.Bitmap
import com.snatik.matches.common.Shared
import com.snatik.matches.themes.Themes
import com.snatik.matches.utils.Utils

/**
 * Before game starts, engine build new board
 *
 * @author sromku
 */
class BoardArrangment {
    // like {0-2, 4-3, 1-5}
    var pairs: MutableMap<Int?, Int?>? = null

    // like {0-mosters_20, 1-mosters_12, 2-mosters_20, ...}
    var tileUrls: MutableMap<Int?, String?>? = null

    /**
     *
     * @param id
     * The id is the number between 0 and number of possible tiles of
     * this theme
     * @return The Bitmap of the tile
     */
    fun getTileBitmap(id: Int, size: Int): Bitmap? {
        val string = tileUrls!!.get(id)
        if (string!!.contains(Themes.URI_DRAWABLE!!)) {
            val drawableResourceName = string.substring(Themes.URI_DRAWABLE!!.length)
            val drawableResourceId = Shared.context!!.resources.getIdentifier(drawableResourceName, "drawable", Shared.context!!.packageName)
            val bitmap = Utils.scaleDown(drawableResourceId, size, size)
            return Utils.crop(bitmap, size, size)
        }
        return null
    }

    fun isPair(id1: Int, id2: Int): Boolean {
        val integer = pairs!!.get(id1)
                ?: // TODO Report this bug!!!
                return false
        return integer == id2
    }
}