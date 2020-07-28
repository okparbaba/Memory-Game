package com.snatik.matches.events.engine

import com.snatik.matches.events.AbstractEvent
import com.snatik.matches.events.EventObserver

/**
 * When the 'back to menu' was pressed.
 */
class HidePairCardsEvent(var id1: Int, var id2: Int) : AbstractEvent() {
    override fun fire(eventObserver: EventObserver?) {
        eventObserver!!.onEvent(this)
    }

    override fun getType(): String? {
        return TYPE
    }

    companion object {
        val TYPE = HidePairCardsEvent::class.java.name
    }

}