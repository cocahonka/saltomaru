package com.cocahonka.saltomaru.salt.item

import com.cocahonka.saltomaru.base.minecraft.SaltomaruItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Класс солевого предмета (соль)
 */
class SaltPiece : SaltomaruItem {

    override val displayName = "Кусочек соли"
    override val lore = "Salt piece"
    override val loreComponent = Component.text(lore).color(NamedTextColor.GRAY)
    override val nameComponent = Component.text(displayName)
    override val material = Material.RABBIT_FOOT
    override val customModelData = 1
    
    override fun getNewItemStack(amount: Int): ItemStack {
        val saltPiece = ItemStack(material, amount)
        val meta = saltPiece.itemMeta

        meta.setCustomModelData(customModelData)
        meta.lore(listOf(loreComponent))
        meta.displayName(nameComponent)

        saltPiece.itemMeta = meta

        return saltPiece
    }
}