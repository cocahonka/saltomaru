package com.cocahonka.saltomaru.base

import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent

interface SaltomaruBlockEvent : SaltomaruEvent {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent)

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent)

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent)
}