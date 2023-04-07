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
        val nameComponent = Component.text(DISPLAY_NAME)
        val material = Material.LEATHER_HELMET

        fun isSaltHelmetItem(item: ItemStack): Boolean {
            val meta = item.itemMeta
            return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
        }

        fun getNewItemStack(amount: Int = 1): ItemStack {
            val saltHelmet = ItemStack(material, amount)
            val meta = saltHelmet.itemMeta

            meta.addEnchant(Enchantment.OXYGEN, 1, true)
            meta.lore(listOf(loreComponent))
            meta.displayName(nameComponent)

            saltHelmet.itemMeta = meta

            return saltHelmet
        }
    }

    internal fun registerSaltHelmetRecipe() {
        val saltHelmet = getNewItemStack()

        val recipe = ShapedRecipe(recipeKey, saltHelmet)
        recipe.shape("XXX", "X X")
        recipe.setIngredient('X', SaltPiece.material)

        Bukkit.addRecipe(recipe)
    }
}