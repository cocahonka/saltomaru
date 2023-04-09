package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.salt.SaltBlock
import com.cocahonka.saltomaru.salt.SaltPiece
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent


class SaltBlockBreakListener : Listener {

    private val validTools = listOf(
        Material.NETHERITE_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.STONE_PICKAXE,
    )

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (SaltBlock.isSaltBlock(block)) {
            event.isCancelled = true
            block.type = Material.AIR
            val player = event.player
            val tool = player.inventory.itemInMainHand

            block.world.playSound(block.location, SaltBlock.getBreakSound(), 1.0f, 1.0f)

            if (!validTools.contains(tool.type)) return

            if (tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
                val saltBlockItem = SaltBlock.getNewItemStack()

                event.block.world.dropItemNaturally(event.block.location, saltBlockItem)
            } else {
                val fortuneLevel = tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)
                val saltPiecesAmount = SaltBlock.getRandomSaltPiecesAmount(fortuneLevel)

                val saltPieceItem = SaltPiece.getNewItemStack(saltPiecesAmount)

                block.world.dropItemNaturally(block.location, saltPieceItem)
            }
        }
    }

}