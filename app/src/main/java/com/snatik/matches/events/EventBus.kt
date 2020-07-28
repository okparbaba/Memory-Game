package com.snatik.matches.events

import android.os.Handler
import com.snatik.matches.events.EventBus
import java.util.*

/**
 * The gateway of all events running in the game from ui to engine components
 * and back.
 *
 * @author sromku
 */
class EventBus private constructor() {
    private val mHandler: Handler?
    private val events = Collections.synchronizedMap(HashMap<String?, MutableList<EventObserver?>?>())
    private val obj: Any? = Any()

    @Synchronized
    fun listen(eventType: String?, eventObserver: EventObserver?) {
        var observers = events[eventType]
        if (observers == null) {
            observers = Collections.synchronizedList(ArrayList())
        }
        observers!!.add(eventObserver)
        events[eventType] = observers
    }

    @Synchronized
    fun unlisten(eventType: String?, eventObserver: EventObserver?) {
        val observers = events[eventType]
        observers?.remove(eventObserver)
    }

    fun notify(event: Event?) {
        synchronized(obj!!) {
            val observers = events[event!!.getType()]
            if (observers != null) {
                for (observer in observers) {
                    val abstractEvent = event as AbstractEvent?
                    abstractEvent!!.fire(observer)
                }
            }
        }
    }

    fun notify(event: Event?, delay: Long) {
        mHandler!!.postDelayed(Runnable { this@EventBus.notify(event) }, delay)
    }

    companion object {
        private var mInstance: EventBus? = null
        fun getInstance(): EventBus? {
            if (mInstance == null) {
                mInstance = EventBus()
            }
            return mInstance
        }
    }

    init {
        mHandler = Handler()
    }
}