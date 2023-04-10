package com.cocahonka.saltomaru.base

import org.bukkit.NamespacedKey
import org.bukkit.event.inventory.PrepareItemCraftEvent

abstract class SaltomaruItemCraftable : SaltomaruItem() {
    abstract val recipeKey: NamespacedKey

    /// Must registerRecipe
    abstract fun onInit()

    abstract fun registerItemRecipe(): Boolean

    abstract fun onPrepareItemCraft(event: PrepareItemCraftEvent)
}