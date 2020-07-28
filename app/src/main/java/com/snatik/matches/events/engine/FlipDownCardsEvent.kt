package com.snatik.matches.events.engine

import com.snatik.matches.events.AbstractEvent
import com.snatik.matches.events.EventObserver

/**
 * When the 'back to menu' was pressed.
 */
class FlipDownCardsEvent : AbstractEvent() {
    override fun fire(eventObserver: EventObserver?) {
        eventObserver!!.onEvent(this)
    }

    override fun getType(): String? {
        return TYPE
    }

    companion object {
        val TYPE = FlipDownCardsEvent::class.java.name
    }
}