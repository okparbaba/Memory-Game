package com.snatik.matches.events.ui

import com.snatik.matches.events.AbstractEvent
import com.snatik.matches.events.EventObserver

/**
 * When the 'back to menu' was pressed.
 */
class DifficultySelectedEvent(val difficulty: Int) : AbstractEvent() {
    override fun fire(eventObserver: EventObserver?) {
        eventObserver!!.onEvent(this)
    }

    override fun getType(): String? {
        return TYPE
    }

    companion object {
        val TYPE = DifficultySelectedEvent::class.java.name
    }

}