package com.cocahonka.saltomaru.utils

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack

abstract class SaltomaruCraftingUtils {
    companion object {
        fun isValidMatrix(
            matrix: Array<out ItemStack?>,
            itemValidator: (ItemStack) -> Boolean,
        ): Boolean {
            for (item in matrix) {
                if (!(item == null || itemValidator(item))) {
                    return false
                }
            }
            return true
        }

        fun retrievePlayerCraft(inventory: CraftingInventory, player: Player) {
            retrieveMatrix(inventory.matrix, player)
            clearMatrix(inventory)
        }

        private fun retrieveMatrix(matrix: Array<out ItemStack?>, player: Player) {
            for (item in matrix) {
                if(item != null) {
                    val remaining = player.inventory.addItem(item)
                    if (remaining.isNotEmpty()) {
                        for (leftover in remaining.values) {
                            player.world.dropItemNaturally(player.location, leftover)
                        }
                    }
                }
            }
        }

        private fun clearMatrix(inventory: CraftingInventory) {
            for (item in inventory.matrix) {
                if(item != null) {
                    inventory.remove(item.type)
                }
            }
        }
    }
}