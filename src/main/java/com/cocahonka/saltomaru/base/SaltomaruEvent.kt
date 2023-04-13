package com.cocahonka.saltomaru.base

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

interface SaltomaruEvent : Listener {
    fun registerEvent(plugin: Plugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
}