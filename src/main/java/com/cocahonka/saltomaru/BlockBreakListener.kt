package com.cocahonka.saltomaru

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class BlockBreakListener : Listener {
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (isSaltOre(block)) {
            event.isCancelled = true // Отмена дропа блока
            block.type = Material.AIR // Удаление блока
            val player = event.player
            val tool = player.inventory.itemInMainHand
            val fortuneLevel = tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)

            val saltParticles = getRandomSaltParticles(fortuneLevel)
            block.world.dropItemNaturally(block.location, ItemStack(Material.SUGAR, saltParticles)) // Замените Material.SUGAR на ваш кастомный предмет для солевых частиц
        }
    }

    private fun getRandomSaltParticles(fortuneLevel: Int): Int {
        val baseAmount = (1..3).random()
        val extra = if (fortuneLevel > 0) (0..fortuneLevel).random() else 0
        return baseAmount + extra
    }

    private fun isSaltOre(block: Block): Boolean {
        if (block.type != Material.WHITE_GLAZED_TERRACOTTA) {
            return false
        }

        val blockData = block.blockData
        return blockData is Directional && blockData.facing == BlockFace.NORTH
    }

}
