package com.snatik.matches.events

import com.snatik.matches.events.engine.FlipDownCardsEvent
import com.snatik.matches.events.engine.GameWonEvent
import com.snatik.matches.events.engine.HidePairCardsEvent
import com.snatik.matches.events.ui.*

interface EventObserver {
    open fun onEvent(event: FlipCardEvent?)
    open fun onEvent(event: DifficultySelectedEvent?)
    open fun onEvent(event: HidePairCardsEvent?)
    open fun onEvent(event: FlipDownCardsEvent?)
    open fun onEvent(event: StartEvent?)
    open fun onEvent(event: ThemeSelectedEvent?)
    open fun onEvent(event: GameWonEvent?)
    open fun onEvent(event: BackGameEvent?)
    open fun onEvent(event: NextGameEvent?)
    open fun onEvent(event: ResetBackgroundEvent?)
}