package com.cocahonka.saltomaru.salt_block

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SaltPiece {
    companion object {
        const val DISPLAY_NAME = "Кусочек соли"
        const val LORE = "Salt piece"
        val loreComponent = Component.text(LORE).color(NamedTextColor.GRAY)
        val nameComponent = Component.text(DISPLAY_NAME)
        val material = Material.RABBIT_FOOT

        fun isSaltPieceItem(item: ItemStack): Boolean {
            val meta = item.itemMeta
            return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
        }

        fun getNewItemStack(amount: Int = 1): ItemStack {
            val saltPiece = ItemStack(material, amount)
            val meta = saltPiece.itemMeta

            meta.lore(listOf(loreComponent))
            meta.displayName(nameComponent)

            saltPiece.itemMeta = meta

            return saltPiece
        }
    }
}