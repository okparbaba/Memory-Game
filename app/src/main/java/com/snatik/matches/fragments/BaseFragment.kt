package com.snatik.matches.fragments

import android.support.v4.app.Fragment
import com.snatik.matches.events.EventObserver
import com.snatik.matches.events.engine.FlipDownCardsEvent
import com.snatik.matches.events.engine.GameWonEvent
import com.snatik.matches.events.engine.HidePairCardsEvent
import com.snatik.matches.events.ui.*

open class BaseFragment : Fragment(), EventObserver {
    override fun onEvent(event: FlipCardEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: DifficultySelectedEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: HidePairCardsEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: FlipDownCardsEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: StartEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: ThemeSelectedEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: GameWonEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: BackGameEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: NextGameEvent?) {
        throw UnsupportedOperationException()
    }

    override fun onEvent(event: ResetBackgroundEvent?) {
        throw UnsupportedOperationException()
    }
}