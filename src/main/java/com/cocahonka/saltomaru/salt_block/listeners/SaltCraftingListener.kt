package com.cocahonka.saltomaru.salt_block.listeners

import com.cocahonka.saltomaru.salt_block.SaltHelmet
import org.bukkit.Bukkit
import org.bukkit.Keyed
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe

class SaltCraftingListener(private val saltPieceHelmetKey: NamespacedKey) : Listener {

    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        val recipe = event.recipe

        if (recipe is Keyed && recipe.key == saltPieceHelmetKey) {
            event.inventory.result = createSaltPieceHelmet()
        }
    }


    private fun createSaltPieceHelmet(): ItemStack {
        val saltPieceHelmet = ItemStack(Material.LEATHER_HELMET)
        val meta = saltPieceHelmet.itemMeta
        meta.lore(listOf(SaltHelmet.loreComponent))
        meta.addEnchant(Enchantment.OXYGEN, 1, true)
        saltPieceHelmet.itemMeta = meta
        return saltPieceHelmet
    }
}
