package com.cocahonka.saltomaru.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack

/**
 * Вспомогательный статический класс для крафта предметов
 */
object SaltomaruCraftingUtils {

    /**
     * Функция проверяющая валидность предметы на матрице крафта
     * @param matrix матрица крафта (массив nullable предметов)
     * @param itemValidator валидатор предметов
     * @return возвращает true если все предметы в матрице крафта прошли [itemValidator]
     */
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

    /**
     * Фукнция чистки крафтого инвентаря
     *
     * Удаляет премедты с матрицы крафта и возвращает их игроку
     * @param inventory крафтовый инвентарь
     * @param player игрок который крафтил
     */
    fun retrievePlayerCraft(inventory: CraftingInventory, player: Player) {
        retrieveMatrix(inventory.matrix, player)
        clearMatrix(inventory)
    }

    /**
     * Функция возврата предметов с матрицы крафта игроку
     *
     * ВАЖНО!
     *
     * Вызов этой функции ДО `inventory.result = null` в `onPrepareItemCraft` НЕДОПУСТИМО
     * @param matrix матрица крафта
     * @param player игрок который крафтил
     */
    private fun retrieveMatrix(matrix: Array<out ItemStack?>, player: Player) {
        for (item in matrix) {
            if (item != null) {
                val remaining = player.inventory.addItem(item)
                if (remaining.isNotEmpty()) {
                    for (leftover in remaining.values) {
                        player.world.dropItemNaturally(player.location, leftover)
                    }
                }
            }
        }
    }

    /**
     * Функция очистики матрицы крафта от предметов в ней
     * @param inventory крафтовый инвентарь
     */
    private fun clearMatrix(inventory: CraftingInventory) {
        for (item in inventory.matrix) {
            if (item != null) {
                inventory.remove(item.type)
            }
        }
    }

}