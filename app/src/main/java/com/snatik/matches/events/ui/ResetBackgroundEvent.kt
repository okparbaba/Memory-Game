package com.snatik.matches.events.ui

import com.snatik.matches.events.AbstractEvent
import com.snatik.matches.events.EventObserver

/**
 * When the 'back to menu' was pressed.
 */
class ResetBackgroundEvent : AbstractEvent() {
    override fun fire(eventObserver: EventObserver?) {
        eventObserver!!.onEvent(this)
    }

    override fun getType(): String? {
        return TYPE
    }

    companion object {
        val TYPE = ResetBackgroundEvent::class.java.name
    }
}