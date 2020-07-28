package com.snatik.matches.events

abstract class AbstractEvent : Event {
    abstract fun fire(eventObserver: EventObserver?)
}