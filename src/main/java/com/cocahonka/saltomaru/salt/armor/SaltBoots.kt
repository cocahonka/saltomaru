package com.cocahonka.saltomaru.salt.armor

import com.cocahonka.saltomaru.base.minecraft.SaltomaruItemCraftable
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
 * Класс солевых ботинок
 * @param plugin главный класс плагина
 * @param saltPiece объект [SaltPiece] предмет соли
 * @property secondRecipeKey второй ключ крафта (дополнительный рецепт)
 */
class SaltBoots(plugin: Plugin, private val saltPiece: SaltPiece) : SaltomaruItemCraftable {

    override val displayName = "Солевые ботинки"
    override val lore = "Salt boots"
    override val loreComponent = Component.text(lore).color(NamedTextColor.GRAY)
    override val nameComponent = Component.text(displayName)
    override val material = Material.LEATHER_BOOTS
    override val customModelData = 1
    override val recipeKey = NamespacedKey(plugin, "salt_boots_1")
    private val secondRecipeKey = NamespacedKey(plugin, "salt_boots_2")


    override fun getNewItemStack(amount: Int): ItemStack {
        val saltBoots = ItemStack(material, amount)
        val meta = saltBoots.itemMeta

        meta.addEnchant(Enchantment.SOUL_SPEED, 3, true)
        meta.lore(listOf(loreComponent))
        meta.displayName(nameComponent)

        saltBoots.itemMeta = meta

        return saltBoots
    }

    override fun registerItemRecipe(): Boolean {
        val saltBoots = getNewItemStack()

        val recipe = ShapedRecipe(recipeKey, saltBoots)
        val secondRecipe = ShapedRecipe(secondRecipeKey, saltBoots)

        recipe.shape("   ", "X X", "X X")
        secondRecipe.shape("X X", "X X", "   ")

        recipe.setIngredient('X', saltPiece.material)
        secondRecipe.setIngredient('X', saltPiece.material)

        return Bukkit.addRecipe(recipe) && Bukkit.addRecipe(secondRecipe)
    }

    @EventHandler
    override fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        val recipe = event.recipe
        val inventory = event.inventory
        val player = event.viewers[0] as Player

        if (recipe is Keyed && (recipe.key == recipeKey || recipe.key == secondRecipeKey)) {
            if (SaltomaruCraftingUtils.isValidMatrix(inventory.matrix, saltPiece::isValidItem)) {
                inventory.result = getNewItemStack()
            } else {
                inventory.result = null
                SaltomaruCraftingUtils.retrievePlayerCraft(inventory, player)
            }

        }
    }
}