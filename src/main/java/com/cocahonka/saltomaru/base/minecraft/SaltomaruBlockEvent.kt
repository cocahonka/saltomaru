package com.cocahonka.saltomaru.base.minecraft

import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent

/**
 * Интерфейс событий для кастомных блоков
 */
interface SaltomaruBlockEvent : SaltomaruEvent {

    /**
     * Функция обрабатывающая событие ломание блока
     * @param event событие ломания блока
     */
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent)

    /**
     * Функция обрабатывающая событие установки блока в мире
     * @param event событие установки блока
     */
    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent)

    /**
     * Функция обрабатывающая событие взрыва энтини (нужно для обработки взрыва блока)
     * @param event событие взырва энтини
     */
    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent)
}