package com.snatik.matches.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import com.snatik.matches.R
import com.snatik.matches.common.Shared
import com.snatik.matches.events.ui.FlipCardEvent
import com.snatik.matches.model.BoardArrangment
import com.snatik.matches.model.BoardConfiguration
import com.snatik.matches.model.Game
import com.snatik.matches.ui.TileView
import com.snatik.matches.utils.Utils
import java.util.*

class BoardView @JvmOverloads constructor(context: Context?, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {
    private val mRowLayoutParams: LayoutParams? = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    private var mTileLayoutParams: LayoutParams? = null
    private val mScreenWidth: Int
    private val mScreenHeight: Int
    private var mBoardConfiguration: BoardConfiguration? = null
    private var mBoardArrangment: BoardArrangment? = null
    private val mViewReference: MutableMap<Int?, TileView?>?
    private val flippedUp: MutableList<Int?>? = ArrayList()
    private var mLocked = false
    private var mSize = 0
    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun setBoard(game: Game?) {
        mBoardConfiguration = game!!.boardConfiguration
        mBoardArrangment = game.boardArrangment
        // calc prefered tiles in width and height
        var singleMargin = resources.getDimensionPixelSize(R.dimen.card_margin)
        val density = resources.displayMetrics.density
        singleMargin = Math.max((1 * density).toInt(), (singleMargin - mBoardConfiguration!!.difficulty * 2 * density).toInt())
        var sumMargin = 0
        for (row in 0 until mBoardConfiguration!!.numRows) {
            sumMargin += singleMargin * 2
        }
        val tilesHeight = (mScreenHeight - sumMargin) / mBoardConfiguration!!.numRows
        val tilesWidth = (mScreenWidth - sumMargin) / mBoardConfiguration!!.numTilesInRow
        mSize = Math.min(tilesHeight, tilesWidth)
        mTileLayoutParams = LayoutParams(mSize, mSize)
        mTileLayoutParams!!.setMargins(singleMargin, singleMargin, singleMargin, singleMargin)

        // build the ui
        buildBoard()
    }

    /**
     * Build the board
     */
    private fun buildBoard() {
        for (row in 0 until mBoardConfiguration!!.numRows) {
            // add row
            addBoardRow(row)
        }
        clipChildren = false
    }

    private fun addBoardRow(rowNum: Int) {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = HORIZONTAL
        linearLayout.gravity = Gravity.CENTER
        for (tile in 0 until mBoardConfiguration!!.numTilesInRow) {
            addTile(rowNum * mBoardConfiguration!!.numTilesInRow + tile, linearLayout)
        }

        // add to this view
        addView(linearLayout, mRowLayoutParams)
        linearLayout.clipChildren = false
    }

    private fun addTile(id: Int, parent: ViewGroup?) {
        val tileView: TileView? = TileView.Companion.fromXml(context, parent)
        tileView!!.layoutParams = mTileLayoutParams
        parent!!.addView(tileView)
        parent.setClipChildren(false)
        mViewReference!![id] = tileView
        object : AsyncTask<Void?, Void?, Bitmap?>() {
            override fun doInBackground(vararg params: Void?): Bitmap? {
                return mBoardArrangment!!.getTileBitmap(id, mSize)
            }

            override fun onPostExecute(result: Bitmap?) {
                tileView.setTileImage(result)
            }
        }.execute()
        tileView.setOnClickListener {
            if (!mLocked && tileView.isFlippedDown()) {
                tileView.flipUp()
                flippedUp!!.add(id)
                if (flippedUp.size == 2) {
                    mLocked = true
                }
                Shared.eventBus!!.notify(FlipCardEvent(id))
            }
        }
        val scaleXAnimator = ObjectAnimator.ofFloat(tileView, "scaleX", 0.8f, 1f)
        scaleXAnimator.interpolator = BounceInterpolator()
        val scaleYAnimator = ObjectAnimator.ofFloat(tileView, "scaleY", 0.8f, 1f)
        scaleYAnimator.interpolator = BounceInterpolator()
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator)
        animatorSet.duration = 500
        tileView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        animatorSet.start()
    }

    fun flipDownAll() {
        for (id in flippedUp!!) {
            mViewReference!!.get(id)!!.flipDown()
        }
        flippedUp.clear()
        mLocked = false
    }

    fun hideCards(id1: Int, id2: Int) {
        animateHide(mViewReference!!.get(id1))
        animateHide(mViewReference.get(id2))
        flippedUp!!.clear()
        mLocked = false
    }

    protected fun animateHide(v: TileView?) {
        val animator = ObjectAnimator.ofFloat(v, "alpha", 0f)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                v!!.setLayerType(View.LAYER_TYPE_NONE, null)
                v.setVisibility(View.INVISIBLE)
            }
        })
        animator.duration = 100
        v!!.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        animator.start()
    }

    companion object {
        fun fromXml(context: Context?, parent: ViewGroup?): BoardView? {
            return LayoutInflater.from(context).inflate(R.layout.board_view, parent, false) as BoardView
        }
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        val margin = resources.getDimensionPixelSize(R.dimen.margine_top)
        val padding = resources.getDimensionPixelSize(R.dimen.board_padding)
        mScreenHeight = resources.displayMetrics.heightPixels - margin - padding * 2
        mScreenWidth = resources.displayMetrics.widthPixels - padding * 2 - Utils.px(20)
        mViewReference = HashMap()
        clipToPadding = false
    }
}