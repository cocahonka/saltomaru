package com.cocahonka.saltomaru.salt.armor

import com.cocahonka.saltomaru.managers.SaltomaruCraftingManager
import com.cocahonka.saltomaru.base.SaltomaruItemCraftable
import com.cocahonka.saltomaru.salt.item.SaltPiece
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Keyed
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.Plugin

class SaltHelmet(plugin: Plugin, private val saltPiece: SaltPiece) : SaltomaruItemCraftable {

    override val displayName = "Солевая шапка"
    override val lore = "Salt helmet"
    override val loreComponent = Component.text(lore).color(NamedTextColor.GRAY)
    override val nameComponent = Component.text(displayName)
    override val material = Material.LEATHER_HELMET
    override val customModelData = 1
    override val recipeKey = NamespacedKey(plugin, "salt_helmet")


    override fun getNewItemStack(amount: Int): ItemStack {
        val saltHelmet = ItemStack(material, amount)
        val meta = saltHelmet.itemMeta

        meta.addEnchant(Enchantment.OXYGEN, 1, true)
        meta.lore(listOf(loreComponent))
        meta.displayName(nameComponent)

        saltHelmet.itemMeta = meta

        return saltHelmet
    }

    override fun registerItemRecipe(): Boolean {
        val saltHelmet = getNewItemStack()

        val recipe = ShapedRecipe(recipeKey, saltHelmet)
        recipe.shape("XXX", "X X")
        recipe.setIngredient('X', saltPiece.material)

        return Bukkit.addRecipe(recipe)
    }

    @EventHandler
    override fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        val recipe = event.recipe
        val inventory = event.inventory
        val player = event.viewers[0] as Player

        if (recipe is Keyed && recipe.key == recipeKey) {
            if (SaltomaruCraftingManager.isValidMatrix(inventory.matrix, saltPiece::isValidItem)) {
                inventory.result = getNewItemStack()
            } else {
                inventory.result = null
                SaltomaruCraftingManager.retrievePlayerCraft(inventory, player)
            }

        }
    }
}