package com.cocahonka.saltomaru.salt_block

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack

class SaltPiece {
    companion object {
        const val DISPLAY_NAME = "Кусочек соли"
        const val LORE = "Salt piece"
        internal val loreComponent = Component.text(LORE).color(NamedTextColor.GRAY)
        internal fun hasSaltPieceLore(item: ItemStack): Boolean {
            val meta = item.itemMeta
            return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
        }
    }
}