package com.cocahonka.saltomaru.salt_block

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class SaltHelmet(private val recipeKey: NamespacedKey) {
    companion object {
        const val DISPLAY_NAME = "Солевая шапка"
        const val LORE = "Salt helmet"
        val loreComponent = Component.text(LORE).color(NamedTextColor.GRAY)
        val nameComponent = Component.text(SaltBlock.DISPLAY_NAME)

        fun isSaltHelmet(item: ItemStack): Boolean {
            val meta = item.itemMeta
            return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
        }

        fun getNewItemStack(n: Int = 1): ItemStack {
            val saltHelmet = ItemStack(Material.LEATHER_HELMET, n)
            val meta = saltHelmet.itemMeta

            meta.addEnchant(Enchantment.OXYGEN, 1, true)
            meta.lore(listOf(loreComponent))
            meta.displayName(nameComponent)

            saltHelmet.itemMeta = meta

            return saltHelmet
        }
    }

    fun registerSaltHelmetRecipe() {
        val saltHelmet = getNewItemStack()

        val recipe = ShapedRecipe(recipeKey, saltHelmet)
        recipe.shape("XXX", "X X")
        recipe.setIngredient('X', Material.RABBIT_FOOT)

        Bukkit.addRecipe(recipe)
    }
}