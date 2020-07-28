package com.snatik.matches.events.engine

import com.snatik.matches.events.AbstractEvent
import com.snatik.matches.events.EventObserver
import com.snatik.matches.model.GameState

/**
 * When the 'back to menu' was pressed.
 */
class GameWonEvent(var gameState: GameState?) : AbstractEvent() {
    override fun fire(eventObserver: EventObserver?) {
        eventObserver!!.onEvent(this)
    }

    override fun getType(): String? {
        return TYPE
    }

    companion object {
        val TYPE = GameWonEvent::class.java.name
    }

}