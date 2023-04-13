package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.managers.SaltomaruCraftingManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent


class CraftingListener(private val craftingManager: SaltomaruCraftingManager) : Listener {

    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
//        craftingManager.saltomaruItems.forEach { it.onPrepareItemCraft(event) }
    }
}
