package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.managers.SaltomaruBlockManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlaceListener(private val blockManager: SaltomaruBlockManager) : Listener {

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        blockManager.saltomaruBlocks.forEach { it.onBlockPlace(event) }
    }
}
