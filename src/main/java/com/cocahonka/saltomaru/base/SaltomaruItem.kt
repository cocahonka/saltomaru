package com.cocahonka.saltomaru.base

import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface SaltomaruItem {
    val displayName: String
    val lore: String
    val loreComponent: TextComponent
    val nameComponent: TextComponent
    val material: Material
    val customModelData: Int

    fun isValidItem(item: ItemStack): Boolean {
        val meta = item.itemMeta
        return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
    }

    fun getNewItemStack(amount: Int = 1): ItemStack

}

