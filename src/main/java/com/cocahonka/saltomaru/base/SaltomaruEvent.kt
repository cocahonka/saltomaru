package com.cocahonka.saltomaru.base

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * Интерфейс кастомных событий в майнкрафте
 *
 * Каждый созданный [SaltomaruEvent] должен быть зарегистрирован в главном классе плагина
 * при помощи [registerEvent]
 */
interface SaltomaruEvent : Listener {

    /**
     * Функция регистрации кастомного события
     * @param plugin главный класс плагина
     * Функция должна быть вызвана в главном классе плагина
     */
    fun registerEvent(plugin: Plugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }
}