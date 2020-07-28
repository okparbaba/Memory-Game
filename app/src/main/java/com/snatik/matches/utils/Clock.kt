package com.snatik.matches.utils

import android.util.Log

/**
 * This is tool for running timer clock with option of pause.<br></br>
 * <br></br>
 *
 * **Important:** If you run this tool on a new thread, don't forget to add:
 * `Looper.prepare()` and `Looper.loop()`
 *
 * <pre>
 * ``
 * new Thread(new Runnable()
 * {
 * public void run()
 * {
 * Looper.prepare();
 * ClockTools.getInstance().startTimer(pTimerSeconds * 1000, 1000, new ClockTools.OnTimerCount()
 * {
 * public void onTick(long millisUntilFinished)
 * {
 * // something
 * }
 *
 * public void onFinish()
 * {
 *
 * }
 * });
 * Looper.loop();
 * }
 * }).start();
 *
 * <pre>
 *
 * @author sromku
</pre></pre> */
class Clock private constructor() {
    class PauseTimer(millisOnTimer: Long, countDownInterval: Long, runAtStart: Boolean, onTimerCount: OnTimerCount?) : CountDownClock(millisOnTimer, countDownInterval, runAtStart) {
        var mOnTimerCount: OnTimerCount? = null
        override fun onTick(millisUntilFinished: Long) {
            mOnTimerCount?.onTick(millisUntilFinished)
        }

        override fun onFinish() {
            mOnTimerCount?.onFinish()
        }

        init {
            mOnTimerCount = onTimerCount
        }
    }

    /**
     * Start timer
     *
     * @param millisOnTimer
     * @param countDownInterval
     */
    fun startTimer(millisOnTimer: Long, countDownInterval: Long, onTimerCount: OnTimerCount?) {
        mPauseTimer?.cancel()
        mPauseTimer = PauseTimer(millisOnTimer, countDownInterval, true, onTimerCount)
        mPauseTimer!!.create()
    }

    /**
     * Pause
     */
    fun pause() {
        mPauseTimer?.pause()
    }

    /**
     * Resume
     */
    fun resume() {
        mPauseTimer?.resume()
    }

    /**
     * Stop and cancel the timer
     */
    fun cancel() {
        if (mPauseTimer != null) {
            mPauseTimer!!.mOnTimerCount = null
            mPauseTimer!!.cancel()
        }
    }

    fun getPassedTime(): Long {
        return mPauseTimer!!.timePassed()
    }

    interface OnTimerCount {
        open fun onTick(millisUntilFinished: Long)
        open fun onFinish()
    }

    companion object {
        private var mPauseTimer: PauseTimer? = null
        private var mInstance: Clock? = null
        fun getInstance(): Clock? {
            if (mInstance == null) {
                mInstance = Clock()
            }
            return mInstance
        }
    }

    init {
        Log.i("my_tag", "NEW INSTANCE OF CLOCK")
    }
}