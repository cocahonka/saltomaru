package com.cocahonka.saltomaru.managers

import com.google.common.collect.ImmutableList
import org.bukkit.event.Listener

class SaltomaruEventManager {
    private val events = mutableListOf<Listener>()
    val saltomaruEvents: ImmutableList<Listener>
        get() = ImmutableList.copyOf(events)

    fun addSaltomaruEvent(event: Listener){
        events.add(event)
    }
}