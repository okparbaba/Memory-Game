package com.snatik.matches.common

import android.content.Context
import android.support.v4.app.FragmentActivity
import com.snatik.matches.engine.Engine
import com.snatik.matches.events.EventBus

object Shared {
    var context: Context? = null
    var activity // it's fine for this app, but better move to weak reference
            : FragmentActivity? = null
    var engine: Engine? = null
    var eventBus: EventBus? = null
}