package com.cocahonka.saltomaru.base

import org.bukkit.NamespacedKey
import org.bukkit.event.inventory.PrepareItemCraftEvent

interface SaltomaruItemCraftable : SaltomaruItem {
    val recipeKey: NamespacedKey

    /// Must registerRecipe
    fun onInit()

    fun registerItemRecipe(): Boolean

    fun onPrepareItemCraft(event: PrepareItemCraftEvent)
}