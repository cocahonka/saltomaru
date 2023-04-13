package com.cocahonka.saltomaru.events.salt

import com.cocahonka.saltomaru.base.SaltomaruEvent
import com.cocahonka.saltomaru.salt.item.SaltPiece
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.Random

class TreeBarkSaltEvent(private val saltPiece: SaltPiece) : SaltomaruEvent {
    companion object {
        private const val CHANCE_TO_DROP = 0.35
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

    private fun getRandomSaltPieceAmount(): Int {
        val amount = when {
            (random.nextDouble() < SUPER_LARGE_DROP_CHANCE) -> (20..42).random()
            (random.nextDouble() < LARGE_DROP_CHANCE) -> (3..6).random()
            random.nextDouble() < MEDIUM_DROP_CHANCE ->  (2..3).random()
            else -> 1
        }
        return amount
    }

    private fun isStrippableLog(material: Material): Boolean {
        return validStrippableLogs.contains(material)
    }

    private fun isValidAxe(item: ItemStack): Boolean {
        val material = item.type
        val isAxe = validAxes.contains(material)
        val hasSharpness = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) >= 1
        val hasSilkTouch = item.containsEnchantment(Enchantment.SILK_TOUCH)

        return isAxe && hasSharpness && hasSilkTouch
    }
}