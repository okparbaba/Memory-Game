package com.snatik.matches.utils

import android.content.Context
import android.graphics.Typeface
import android.util.SparseArray
import android.widget.TextView

object FontLoader {
    const val GROBOLD = 0
    private val fonts: SparseArray<Typeface?>? = SparseArray()
    private var fontsLoaded = false
    fun loadFonts(context: Context?) {
        for (i in Font.values().indices) {
            fonts!!.put(i, Typeface.createFromAsset(context!!.getAssets(), Font.getByVal(i)))
        }
        fontsLoaded = true
    }

    /**
     * Returns a loaded custom font based on it's identifier.
     *
     * @param context
     * - the current context
     * @param fontIdentifier
     * = the identifier of the requested font
     *
     * @return Typeface object of the requested font.
     */
    fun getTypeface(context: Context?, font: Font?): Typeface? {
        if (!fontsLoaded) {
            loadFonts(context)
        }
        return fonts!!.get(font!!.`val`)
    }

    /**
     * Set the given font into the array of text views
     *
     * @param context
     * - the current context
     * @param textViews
     * - array of text views to set
     * @param fontIdentifier
     * = the identifier of the requested font
     */
    fun setTypeface(context: Context?, textViews: Array<TextView?>?, font: Font?) {
        setTypeFaceToTextViews(context, textViews, font, Typeface.NORMAL)
    }

    /**
     * Set the given bold font into the array of text views
     *
     * @param context
     * - the current context
     * @param textViews
     * - array of text views to set
     * @param fontIdentifier
     * = the identifier of the requested font
     */
    fun setBoldTypeface(context: Context?, textViews: Array<TextView?>?, font: Font?) {
        setTypeFaceToTextViews(context, textViews, font, Typeface.BOLD)
    }

    private fun setTypeFaceToTextViews(context: Context?, textViews: Array<TextView?>?, font: Font?, fontStyle: Int) {
        if (!fontsLoaded) {
            loadFonts(context)
        }
        val currentFont = fonts!!.get(font!!.`val`)
        for (i in textViews!!.indices) {
            if (textViews.get(i) != null) textViews.get(i)!!.setTypeface(currentFont, fontStyle)
        }
    }

    enum class Font(val `val`: Int, private val path: String?) {
        GROBOLD(FontLoader.GROBOLD, "fonts/grobold.ttf");

        companion object {
            fun getByVal(`val`: Int): String? {
                for (font in values()) {
                    if (font.`val` == `val`) {
                        return font.path
                    }
                }
                return null
            }
        }

    }
}