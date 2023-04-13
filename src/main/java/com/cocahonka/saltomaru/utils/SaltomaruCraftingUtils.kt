package com.cocahonka.saltomaru.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack

abstract class SaltomaruCraftingUtils {
    companion object {
        fun isValidMatrix(
            matrix: Array<out ItemStack?>,
            itemValidator: (ItemStack) -> Boolean
        ): Boolean {
            for (item in matrix) {
                if(!(item == null || itemValidator(item))){
                    return false
                }
            }
            return true
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