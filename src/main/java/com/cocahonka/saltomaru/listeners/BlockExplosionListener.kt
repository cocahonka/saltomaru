package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.managers.SaltomaruBlockManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent

class BlockExplosionListener(private val blockManager: SaltomaruBlockManager) : Listener {

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
//       blockManager.saltomaruBlocks.forEach { it.onEntityExplode(event) }
    }
}
