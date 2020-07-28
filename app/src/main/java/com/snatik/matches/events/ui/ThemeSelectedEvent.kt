package com.snatik.matches.events.ui

import com.snatik.matches.events.AbstractEvent
import com.snatik.matches.events.EventObserver
import com.snatik.matches.themes.Theme

class ThemeSelectedEvent(val theme: Theme?) : AbstractEvent() {
    override fun fire(eventObserver: EventObserver?) {
        eventObserver!!.onEvent(this)
    }

    override fun getType(): String? {
        return TYPE
    }

    companion object {
        val TYPE = ThemeSelectedEvent::class.java.name
    }

}