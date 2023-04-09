package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.managers.SaltomaruBlockManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent


class BlockBreakListener(private val blockManager: SaltomaruBlockManager) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        blockManager.saltomaruBlocks.forEach { it.onBlockBreak(event) }
    }

}