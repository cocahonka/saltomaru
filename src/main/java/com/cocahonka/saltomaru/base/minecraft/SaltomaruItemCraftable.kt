package com.cocahonka.saltomaru.base.minecraft

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.PrepareItemCraftEvent

/**
 * Интерфейс для крафта кастомных предметов
 * @property recipeKey основной ключ рецепта (для дальнейшей регистрации крафта)
 */
interface SaltomaruItemCraftable : SaltomaruItem, SaltomaruEvent {
    val recipeKey: NamespacedKey

    /**
     * Функция регистрации рецепта
     * @return возвращает true если предмет успешно зарегистрирован
     */
    fun registerItemRecipe(): Boolean

    /**
     * Событие выкладывания предметов для крафта
     *
     * Нужно для валидации использованных предметов в крафте
     * @param event событие выкладывания предметов для крафта
     */
    @EventHandler
    fun onPrepareItemCraft(event: PrepareItemCraftEvent)
}