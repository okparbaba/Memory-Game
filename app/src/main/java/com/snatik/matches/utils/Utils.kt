package com.snatik.matches.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import android.media.ThumbnailUtils
import com.snatik.matches.common.Shared

object Utils {
    fun px(dp: Int): Int {
        return (Shared.context!!.resources.displayMetrics.density * dp).toInt()
    }

    fun screenWidth(): Int {
        return Shared.context!!.resources.displayMetrics.widthPixels
    }

    fun screenHeight(): Int {
        return Shared.context!!.resources.displayMetrics.heightPixels
    }

    fun crop(source: Bitmap?, newHeight: Int, newWidth: Int): Bitmap? {
        val sourceWidth = source!!.getWidth()
        val sourceHeight = source.getHeight()

        // Compute the scaling factors to fit the new height and width,
        // respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        val xScale = newWidth.toFloat() / sourceWidth
        val yScale = newHeight.toFloat() / sourceHeight
        val scale = Math.max(xScale, yScale)

        // Now get the size of the source bitmap when scaled
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2

        // The target rectangle for the new, scaled version of the source bitmap
        // will now
        // be
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        // Finally, we create a new bitmap of the specified size and draw our
        // new,
        // scaled bitmap onto it.
        val dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig())
        val canvas = Canvas(dest)
        canvas.drawBitmap(source, null, targetRect, null)
        return dest
    }

    fun scaleDown(resource: Int, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(Shared.context!!.resources, resource)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(Shared.context!!.resources, resource, options)
    }

    /**
     * Downscales a bitmap by the specified factor
     */
    fun downscaleBitmap(wallpaper: Bitmap?, factor: Int): Bitmap? {
        // convert to bitmap and get the center
        val widthPixels = wallpaper!!.getWidth() / factor
        val heightPixels = wallpaper.getHeight() / factor
        return ThumbnailUtils.extractThumbnail(wallpaper, widthPixels, heightPixels)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options?, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val height = options!!.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }
}