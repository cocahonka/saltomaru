package com.cocahonka.saltomaru.base

import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

abstract class SaltomaruItem {
    abstract val displayName: String
    abstract val lore: String
    abstract val loreComponent: TextComponent
    abstract val nameComponent: TextComponent
    abstract val material: Material
    abstract val customModelData: Int

    fun isValidItem(item: ItemStack): Boolean {
        val meta = item.itemMeta
        return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
    }

    abstract fun getNewItemStack(amount: Int = 1): ItemStack

}

