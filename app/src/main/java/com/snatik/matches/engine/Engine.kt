package com.snatik.matches.engine

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.AsyncTask
import android.os.Handler
import android.widget.ImageView
import com.snatik.matches.R
import com.snatik.matches.common.Memory
import com.snatik.matches.common.Music
import com.snatik.matches.common.Shared
import com.snatik.matches.engine.ScreenController.Screen
import com.snatik.matches.events.EventObserverAdapter
import com.snatik.matches.events.engine.FlipDownCardsEvent
import com.snatik.matches.events.engine.GameWonEvent
import com.snatik.matches.events.engine.HidePairCardsEvent
import com.snatik.matches.events.ui.*
import com.snatik.matches.model.BoardArrangment
import com.snatik.matches.model.BoardConfiguration
import com.snatik.matches.model.Game
import com.snatik.matches.model.GameState
import com.snatik.matches.themes.Theme
import com.snatik.matches.themes.Themes
import com.snatik.matches.ui.PopupManager
import com.snatik.matches.utils.Clock
import com.snatik.matches.utils.Utils
import java.util.*

class Engine private constructor() : EventObserverAdapter() {
    private var mPlayingGame: Game? = null
    private var mFlippedId = -1
    private var mToFlip = -1
    private val mScreenController: ScreenController?
    private var mSelectedTheme: Theme? = null
    private var mBackgroundImage: ImageView? = null
    private var mHandler: Handler?
    fun start() {
        Shared.eventBus?.listen(DifficultySelectedEvent.Companion.TYPE, this)
        Shared.eventBus?.listen(FlipCardEvent.Companion.TYPE, this)
        Shared.eventBus?.listen(StartEvent.Companion.TYPE, this)
        Shared.eventBus?.listen(ThemeSelectedEvent.Companion.TYPE, this)
        Shared.eventBus?.listen(BackGameEvent.Companion.TYPE, this)
        Shared.eventBus?.listen(NextGameEvent.Companion.TYPE, this)
        Shared.eventBus?.listen(ResetBackgroundEvent.Companion.TYPE, this)
    }

    fun stop() {
        mPlayingGame = null
        mBackgroundImage?.setImageDrawable(null)
        mBackgroundImage = null
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
        Shared.eventBus?.unlisten(DifficultySelectedEvent.Companion.TYPE, this)
        Shared.eventBus?.unlisten(FlipCardEvent.Companion.TYPE, this)
        Shared.eventBus?.unlisten(StartEvent.Companion.TYPE, this)
        Shared.eventBus?.unlisten(ThemeSelectedEvent.Companion.TYPE, this)
        Shared.eventBus?.unlisten(BackGameEvent.Companion.TYPE, this)
        Shared.eventBus?.unlisten(NextGameEvent.Companion.TYPE, this)
        Shared.eventBus?.unlisten(ResetBackgroundEvent.Companion.TYPE, this)
        mInstance = null
    }

    override fun onEvent(event: ResetBackgroundEvent?) {
        val drawable = mBackgroundImage!!.getDrawable()
        if (drawable != null) {
            (drawable as TransitionDrawable).reverseTransition(2000)
        } else {
            object : AsyncTask<Void?, Void?, Bitmap?>() {
                override fun doInBackground(vararg params: Void?): Bitmap? {
                    return Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight())
                }

                override fun onPostExecute(bitmap: Bitmap?) {
                    mBackgroundImage!!.setImageBitmap(bitmap)
                }
            }.execute()
        }
    }

    override fun onEvent(event: StartEvent?) {
        mScreenController?.openScreen(Screen.THEME_SELECT)
    }

    override fun onEvent(event: NextGameEvent?) {
        PopupManager.closePopup()
        var difficulty = mPlayingGame?.boardConfiguration!!.difficulty
        if (mPlayingGame?.gameState?.achievedStars == 3 && difficulty < 6) {
            difficulty++
        }
        Shared.eventBus?.notify(DifficultySelectedEvent(difficulty))
    }

    override fun onEvent(event: BackGameEvent?) {
        PopupManager.closePopup()
        mScreenController?.openScreen(Screen.DIFFICULTY)
    }

    override fun onEvent(event: ThemeSelectedEvent?) {
        mSelectedTheme = event?.theme
        mScreenController?.openScreen(Screen.DIFFICULTY)
        val task: AsyncTask<Void?, Void?, TransitionDrawable?> = object : AsyncTask<Void?, Void?, TransitionDrawable?>() {
            override fun doInBackground(vararg params: Void?): TransitionDrawable? {
                val bitmap = Utils.scaleDown(R.drawable.background, Utils.screenWidth(), Utils.screenHeight())
                var backgroundImage = Themes.getBackgroundImage(mSelectedTheme)
                backgroundImage = Utils.crop(backgroundImage, Utils.screenHeight(), Utils.screenWidth())
                val backgrounds = arrayOfNulls<Drawable?>(2)
                backgrounds[0] = BitmapDrawable(Shared.context?.resources, bitmap)
                backgrounds[1] = BitmapDrawable(Shared.context?.resources, backgroundImage)
                return TransitionDrawable(backgrounds)
            }

            override fun onPostExecute(result: TransitionDrawable?) {
                super.onPostExecute(result)
                mBackgroundImage?.setImageDrawable(result)
                result?.startTransition(2000)
            }
        }
        task.execute()
    }

    override fun onEvent(event: DifficultySelectedEvent?) {
        mFlippedId = -1
        mPlayingGame = Game()
        mPlayingGame?.boardConfiguration = BoardConfiguration(event!!.difficulty)
        mPlayingGame?.theme = mSelectedTheme
        mToFlip = mPlayingGame?.boardConfiguration!!.numTiles

        // arrange board
        arrangeBoard()

        // start the screen
        mScreenController!!.openScreen(Screen.GAME)
    }

    private fun arrangeBoard() {
        val boardConfiguration = mPlayingGame?.boardConfiguration
        val boardArrangment = BoardArrangment()

        // build pairs
        // result {0,1,2,...n} // n-number of tiles
        val ids: MutableList<Int?> = ArrayList()
        for (i in 0 until boardConfiguration?.numTiles!!) {
            ids.add(i)
        }
        // shuffle
        // result {4,10,2,39,...}
        Collections.shuffle(ids)

        // place the board
        val tileImageUrls = mPlayingGame?.theme?.tileImageUrls
        Collections.shuffle(tileImageUrls)
        boardArrangment.pairs = HashMap()
        boardArrangment.tileUrls = HashMap()
        var j = 0
        var i = 0
        while (i < ids.size) {
            if (i + 1 < ids.size) {
                // {4,10}, {2,39}, ...
                boardArrangment.pairs!![ids[i]] = ids[i + 1]
                // {10,4}, {39,2}, ...
                boardArrangment.pairs!![ids[i + 1]] = ids[i]
                // {4,
                boardArrangment.tileUrls!![ids[i]] = tileImageUrls!![j]
                boardArrangment.tileUrls!![ids[i + 1]] = tileImageUrls[j]
                i++
                j++
            }
            i++
        }
        mPlayingGame?.boardArrangment = boardArrangment
    }

    override fun onEvent(event: FlipCardEvent?) {
        // Log.i("my_tag", "Flip: " + event.id);
        val id = event!!.id
        if (mFlippedId == -1) {
            mFlippedId = id
            // Log.i("my_tag", "Flip: mFlippedId: " + event.id);
        } else {
            if (mPlayingGame!!.boardArrangment!!.isPair(mFlippedId, id)) {
                // Log.i("my_tag", "Flip: is pair: " + mFlippedId + ", " + id);
                // send event - hide id1, id2
                Shared.eventBus!!.notify(HidePairCardsEvent(mFlippedId, id), 1000)
                // play music
                mHandler!!.postDelayed(Runnable { Music.playCorrent() }, 1000)
                mToFlip -= 2
                if (mToFlip == 0) {
                    val passedSeconds = (Clock.Companion.getInstance()!!.getPassedTime() / 1000).toInt()
                    Clock.Companion.getInstance()!!.pause()
                    val totalTime = mPlayingGame!!.boardConfiguration!!.time
                    val gameState = GameState()
                    mPlayingGame!!.gameState = gameState
                    // remained seconds
                    gameState.remainedSeconds = totalTime - passedSeconds
                    gameState.passedSeconds = passedSeconds

                    // calc stars
                    if (passedSeconds <= totalTime / 2) {
                        gameState.achievedStars = 3
                    } else if (passedSeconds <= totalTime - totalTime / 5) {
                        gameState.achievedStars = 2
                    } else if (passedSeconds < totalTime) {
                        gameState.achievedStars = 1
                    } else {
                        gameState.achievedStars = 0
                    }

                    // calc score
                    gameState.achievedScore = mPlayingGame!!.boardConfiguration!!.difficulty * gameState.remainedSeconds * mPlayingGame!!.theme!!.id

                    // save to memory
                    Memory.save(mPlayingGame!!.theme!!.id, mPlayingGame!!.boardConfiguration!!.difficulty, gameState.achievedStars)
                    Memory.saveTime(mPlayingGame!!.theme!!.id, mPlayingGame!!.boardConfiguration!!.difficulty, gameState.passedSeconds)
                    Shared.eventBus!!.notify(GameWonEvent(gameState), 1200)
                }
            } else {
                // Log.i("my_tag", "Flip: all down");
                // send event - flip all down
                Shared.eventBus!!.notify(FlipDownCardsEvent(), 1000)
            }
            mFlippedId = -1
            // Log.i("my_tag", "Flip: mFlippedId: " + mFlippedId);
        }
    }

    fun getActiveGame(): Game? {
        return mPlayingGame
    }

    fun getSelectedTheme(): Theme? {
        return mSelectedTheme
    }

    fun setBackgroundImageView(backgroundImage: ImageView?) {
        mBackgroundImage = backgroundImage
    }

    companion object {
        private var mInstance: Engine? = null
        fun getInstance(): Engine? {
            if (mInstance == null) {
                mInstance = Engine()
            }
            return mInstance
        }
    }

    init {
        mScreenController = ScreenController.Companion.getInstance()
        mHandler = Handler()
    }
}