package com.cocahonka.saltomaru.crafting

import com.cocahonka.saltomaru.salt.gear.SaltHelmet
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class SaltCrafting(plugin: Plugin) {

    private val saltHelmetKey = NamespacedKey(plugin, "salt_helmet")
    val saltHelmet = SaltHelmet(saltHelmetKey)

    init {
        saltHelmet.registerSaltHelmetRecipe()
    }

    companion object {
        fun isValidMatrix(
            matrix: Array<out ItemStack?>,
            itemValidator: (ItemStack) -> Boolean
        ): Boolean {
            return matrix.all { item ->
                return if (item == null) {
                    true
                } else {
                    itemValidator(item)
                }
            }
        }

        fun retrievePlayerCraft(inventory: CraftingInventory, player: Player){
            retrieveMatrix(inventory, player)
            clearMatrix(inventory)
        }

        private fun retrieveMatrix(inventory: CraftingInventory, player: Player){
            for (item in inventory.matrix) {
                if (item != null) {
                    player.inventory.addItem(item)
                }
            }
        }

        private fun clearMatrix(inventory: CraftingInventory) {
            for (i in 0 until inventory.size) {
                inventory.setItem(i, null)
            }
        }
    }


}