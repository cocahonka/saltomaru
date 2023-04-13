package com.cocahonka.saltomaru.events.salt

import com.cocahonka.saltomaru.base.SaltomaruEvent
import com.cocahonka.saltomaru.salt.item.SaltPiece
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.Random

/**
 * Класс кастомного события (Выпадение соли из коры деревьев -> после обтёсывания древесины)
 * @param saltPiece объект [SaltPiece] (соль)
 * @property Chances содержить статические поля вероятностей событий
 * @property CHANCE_TO_DROP вероятность выпадения соли (верояность самого события)
 * @property MEDIUM_DROP_CHANCE вероятность выпадения среднего количества соли
 * @property LARGE_DROP_CHANCE вероятность выпадения большого количества соли
 * @property SUPER_LARGE_DROP_CHANCE верояность выпадения огромного количества соли
 * @property random генератор рандомных вероятностей
 * @property validStrippableLogs [Material] лист бревен пригодных для обтёсывания
 * @property validAxes [Material] лист пригодных топоров для обтёсываия
 */
class TreeBarkSaltEvent(private val saltPiece: SaltPiece) : SaltomaruEvent {
    companion object Chances {
        private const val CHANCE_TO_DROP = 0.5
        private const val MEDIUM_DROP_CHANCE = 0.3
        private const val LARGE_DROP_CHANCE = 0.1
        private const val SUPER_LARGE_DROP_CHANCE = 0.02
    }

    private val random = Random()

    private val validStrippableLogs = listOf(
        Material.JUNGLE_LOG,
        Material.DARK_OAK_LOG,
        Material.MANGROVE_LOG,
        Material.ACACIA_LOG,
        Material.OAK_LOG,
        Material.BIRCH_LOG,
        Material.SPRUCE_LOG,
    )

    private val validAxes = listOf(
        Material.WOODEN_AXE,
        Material.STONE_AXE,
        Material.IRON_AXE,
        Material.GOLDEN_AXE,
        Material.DIAMOND_AXE,
        Material.NETHERITE_AXE,
    )

    /**
     * Событие взаимодействия игрока с окружающим миром
     *
     * В данном случае проверяется взаимодействовал ли игрок с блоком древесины
     * и в случае когда рандомная вероятность меньше чем [CHANCE_TO_DROP]
     * с дерева падает соль в количестве определенной функцией [getRandomSaltPieceAmount]
     */
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val clickedBlock = event.clickedBlock
        val itemInHand = player.inventory.itemInMainHand

        if (event.hasBlock() && isStrippableLog(clickedBlock!!.type) && isValidAxe(itemInHand)) {
            if (random.nextDouble() < CHANCE_TO_DROP) {
                val amount = getRandomSaltPieceAmount()
                val saltPiece = saltPiece.getNewItemStack(amount)
                player.world.dropItemNaturally(clickedBlock.location, saltPiece)
            }
        }
    }

    /**
     * Функция для получения рандомного количества соли
     *
     * В зависимости от рандомной вероятности дропу присваивается рандомное количество из диапазона
     * чисел
     *
     * Диапазон определяется принадлежностью рандомной вероятности к вероятностям из [Chances]
     *
     * @return количество соли
     */
    private fun getRandomSaltPieceAmount(): Int {
        val randomDouble = random.nextDouble()
        val amount = when {
            randomDouble < SUPER_LARGE_DROP_CHANCE -> (20..42).random()
            randomDouble < LARGE_DROP_CHANCE -> (3..6).random()
            randomDouble < MEDIUM_DROP_CHANCE ->  (2..3).random()
            else -> 1
        }
        return amount
    }

    /**
     * Функция проверяющая является ли [material] деревом которое можно обтесать
     * @param material материал блока
     * @return возвращает true если [material] есть в [validStrippableLogs]
     */
    private fun isStrippableLog(material: Material): Boolean {
        return validStrippableLogs.contains(material)
    }

    /**
     * Функция проверяющая является ли предмет в руке топором пригодным для события выпадения соли
     *
     * Валидация происходит по факту пренадлежности материала в руке к [validAxes], а также имеющего
     * нужные зачарования
     *
     * @param item предмет
     * @return возвращает true если материал предмета есть в [validAxes] и имеет нужные зачарования
     */
    private fun isValidAxe(item: ItemStack): Boolean {
        val material = item.type
        val isAxe = validAxes.contains(material)
        val hasSharpness = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 1
        val hasSilkTouch = item.containsEnchantment(Enchantment.SILK_TOUCH)

        return isAxe && hasSharpness && hasSilkTouch
    }
}