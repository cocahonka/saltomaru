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
        internal val loreComponent = Component.text(LORE).color(NamedTextColor.GRAY)
        internal fun isSaltHelmet(item: ItemStack): Boolean {
            val meta = item.itemMeta
            return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
        }
    }

    internal fun registerSaltHelmetRecipe() {
        val saltHelmet = ItemStack(Material.LEATHER_HELMET)
        val meta = saltHelmet.itemMeta
        meta.addEnchant(Enchantment.OXYGEN, 1, true)
        saltHelmet.itemMeta = meta

        val recipe = ShapedRecipe(recipeKey, saltHelmet)
        recipe.shape("XXX", "X X")
        recipe.setIngredient('X', Material.RABBIT_FOOT)

        Bukkit.addRecipe(recipe)
    }
}