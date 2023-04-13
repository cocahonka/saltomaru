package com.cocahonka.saltomaru.base

import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Интерфейс для кастомного предмета (item)
 * @property displayName отображаемое имя предмета в инвенторе
 * @property lore подпись предмета (дополнительный текст под предметом)
 * @property loreComponent компонент подписи (копмонент нужен для взаимодействия c метаданными предметов)
 * @property nameComponent компонент имени
 * @property material материал предмета (камень, булыжник, печка и т.д)
 * @property customModelData дополнительная мета-информация для опознания кастомного предмета
 */
interface SaltomaruItem {
    val displayName: String
    val lore: String
    val loreComponent: TextComponent
    val nameComponent: TextComponent
    val material: Material
    val customModelData: Int

    /**
     * Функция опознания кастомного предмета среди ванильных
     *
     * Опознание происходит по [loreComponent] и [material]
     * @param item предмет для опознания
     * @return возращает true если предмет кастомный
     */
    fun isValidItem(item: ItemStack): Boolean {
        if(item.type != material) return false
        val meta = item.itemMeta
        return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
    }

    /**
     * Функция выдачи нового стака кастомного предмета
     * @param amount количество предметов в стаке (обычно 1..64)
     * @return новый стак кастомных предметов
     */
    fun getNewItemStack(amount: Int = 1): ItemStack

}

