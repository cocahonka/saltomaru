package com.cocahonka.saltomaru.managers

import com.cocahonka.saltomaru.base.SaltomaruItemCraftable
import com.google.common.collect.ImmutableList
import org.bukkit.entity.Player
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack

class SaltomaruCraftingManager {

    private val items = mutableListOf<SaltomaruItemCraftable>()
    val saltomaruItems: ImmutableList<SaltomaruItemCraftable>
        get() = ImmutableList.copyOf(items)

    fun addSaltomaruItem(item: SaltomaruItemCraftable){
        items.add(item)
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