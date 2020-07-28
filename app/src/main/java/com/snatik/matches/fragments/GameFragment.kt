package com.snatik.matches.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.snatik.matches.R
import com.snatik.matches.common.Shared
import com.snatik.matches.events.engine.FlipDownCardsEvent
import com.snatik.matches.events.engine.GameWonEvent
import com.snatik.matches.events.engine.HidePairCardsEvent
import com.snatik.matches.ui.BoardView
import com.snatik.matches.ui.PopupManager
import com.snatik.matches.utils.Clock
import com.snatik.matches.utils.Clock.OnTimerCount
import com.snatik.matches.utils.FontLoader

class GameFragment : BaseFragment() {
    private var mBoardView: BoardView? = null
    private var mTime: TextView? = null
    private var mTimeImage: ImageView? = null
    private val ads: LinearLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.game_fragment, container, false) as ViewGroup
        view.clipChildren = false
        (view.findViewById<View?>(R.id.game_board) as ViewGroup).clipChildren = false
        mTime = view.findViewById<View?>(R.id.time_bar_text) as TextView
        mTimeImage = view.findViewById<View?>(R.id.time_bar_image) as ImageView
        FontLoader.setTypeface(Shared.context, arrayOf(mTime), FontLoader.Font.GROBOLD)
        mBoardView = BoardView.Companion.fromXml(activity!!.getApplicationContext(), view)
        val frameLayout = view.findViewById<View?>(R.id.game_container) as FrameLayout
        frameLayout.addView(mBoardView)
        frameLayout.clipChildren = false

        // build board
        buildBoard()
        Shared.eventBus!!.listen(FlipDownCardsEvent.Companion.TYPE, this)
        Shared.eventBus!!.listen(HidePairCardsEvent.Companion.TYPE, this)
        Shared.eventBus!!.listen(GameWonEvent.Companion.TYPE, this)
        return view
    }

    override fun onDestroy() {
        Shared.eventBus!!.unlisten(FlipDownCardsEvent.Companion.TYPE, this)
        Shared.eventBus!!.unlisten(HidePairCardsEvent.Companion.TYPE, this)
        Shared.eventBus!!.unlisten(GameWonEvent.Companion.TYPE, this)
        super.onDestroy()
    }

    private fun buildBoard() {
        val game = Shared.engine!!.getActiveGame()
        val time = game!!.boardConfiguration!!.time
        setTime(time)
        mBoardView!!.setBoard(game)
        startClock(time)
    }

    private fun setTime(time: Int) {
        val min = time / 60
        val sec = time - min * 60
        mTime!!.setText(" " + String.format("%02d", min) + ":" + String.format("%02d", sec))
    }

    private fun startClock(sec: Int) {
        val clock: Clock = Clock.Companion.getInstance()!!
        clock.startTimer(sec * 1000.toLong(), 1000, object : OnTimerCount {
            override fun onTick(millisUntilFinished: Long) {
                setTime((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                setTime(0)
            }
        })
    }

    override fun onEvent(event: GameWonEvent?) {
        mTime!!.setVisibility(View.GONE)
        mTimeImage!!.setVisibility(View.GONE)
        PopupManager.showPopupWon(event!!.gameState)
    }

    override fun onEvent(event: FlipDownCardsEvent?) {
        mBoardView!!.flipDownAll()
    }

    override fun onEvent(event: HidePairCardsEvent?) {
        mBoardView!!.hideCards(event!!.id1, event.id2)
    }
}