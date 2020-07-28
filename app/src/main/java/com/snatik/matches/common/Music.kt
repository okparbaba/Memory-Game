package com.snatik.matches.common

import android.media.MediaPlayer
import com.snatik.matches.R

object Music {
    var OFF = false
    fun playCorrent() {
        if (!OFF) {
            val mp = MediaPlayer.create(Shared.context, R.raw.correct_answer)
            mp.setOnCompletionListener { mp ->
                var mp = mp
                mp.reset()
                mp.release()
                mp = null
            }
            mp.start()
        }
    }

    fun playBackgroundMusic() {
        // TODO
    }

    fun showStar() {
        if (!OFF) {
            val mp = MediaPlayer.create(Shared.context, R.raw.star)
            mp.setOnCompletionListener { mp ->
                var mp = mp
                mp.reset()
                mp.release()
                mp = null
            }
            mp.start()
        }
    }
}