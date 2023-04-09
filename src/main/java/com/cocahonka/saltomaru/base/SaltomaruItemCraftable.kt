package com.cocahonka.saltomaru.base

import org.bukkit.NamespacedKey
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.plugin.Plugin

abstract class SaltomaruItemCraftable(plugin: Plugin) : SaltomaruItem() {
    abstract val recipeKey: NamespacedKey

    /// Must registerRecipe
    abstract fun onInit()

    abstract fun registerItemRecipe(): Boolean

    abstract fun onPrepareItemCraft(event: PrepareItemCraftEvent)
}