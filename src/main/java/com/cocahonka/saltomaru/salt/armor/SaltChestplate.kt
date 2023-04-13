package com.cocahonka.saltomaru.salt.armor

import com.cocahonka.saltomaru.base.SaltomaruItemCraftable
import com.cocahonka.saltomaru.salt.item.SaltPiece
import com.cocahonka.saltomaru.utils.SaltomaruCraftingUtils
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

/**
 * Класс солевого нагрудника
 * @param plugin главный класс плагина
 * @param saltPiece объект [SaltPiece] предмет соли
 */
class SaltChestplate(plugin: Plugin, private val saltPiece: SaltPiece) : SaltomaruItemCraftable {
    override val displayName = "Солевой нагрудник"
    override val lore = "Salt Chestplate"
    override val loreComponent = Component.text(lore).color(NamedTextColor.GRAY)
    override val nameComponent = Component.text(displayName)
    override val material = Material.LEATHER_CHESTPLATE
    override val customModelData = 1
    override val recipeKey = NamespacedKey(plugin, "salt_chestplate")

    override fun getNewItemStack(amount: Int): ItemStack {
        val saltChestplate = ItemStack(material, amount)
        val meta = saltChestplate.itemMeta

        meta.addEnchant(Enchantment.THORNS, 4, true)
        meta.lore(listOf(loreComponent))
        meta.displayName(nameComponent)

        saltChestplate.itemMeta = meta

        return saltChestplate
    }


    override fun registerItemRecipe(): Boolean {
        val saltChestplate = getNewItemStack()

        val recipe = ShapedRecipe(recipeKey, saltChestplate)
        recipe.shape("X X", "XXX", "XXX")
        recipe.setIngredient('X', saltPiece.material)

        return Bukkit.addRecipe(recipe)
    }

    @EventHandler
    override fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        val recipe = event.recipe
        val inventory = event.inventory
        val player = event.viewers[0] as Player

        if (recipe is Keyed && recipe.key == recipeKey) {
            if (SaltomaruCraftingUtils.isValidMatrix(inventory.matrix, saltPiece::isValidItem)) {
                inventory.result = getNewItemStack()
            } else {
                inventory.result = null
                SaltomaruCraftingUtils.retrievePlayerCraft(inventory, player)
            }

        }
    }
}