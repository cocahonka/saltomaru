package com.cocahonka.saltomaru.salt_block.listeners

import com.cocahonka.saltomaru.salt_block.SaltHelmet
import org.bukkit.Keyed
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent


class SaltCraftingListener(private val saltPieceHelmetKey: NamespacedKey) : Listener {

    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        val recipe = event.recipe

        if (recipe is Keyed && recipe.key == saltPieceHelmetKey) {
            event.inventory.result = SaltHelmet.getNewItemStack()
        }
    }

}
