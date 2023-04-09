package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.crafting.SaltCrafting
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent


class SaltCraftingListener(private val craftingManager: SaltCrafting) : Listener {


    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        craftingManager.saltHelmet.onPrepareItemCraft(event)
    }


}
