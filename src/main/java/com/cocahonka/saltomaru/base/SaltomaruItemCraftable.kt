package com.cocahonka.saltomaru.base

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.PrepareItemCraftEvent

interface SaltomaruItemCraftable : SaltomaruItem, SaltomaruEvent {
    val recipeKey: NamespacedKey

    fun registerItemRecipe(): Boolean

    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent)
}