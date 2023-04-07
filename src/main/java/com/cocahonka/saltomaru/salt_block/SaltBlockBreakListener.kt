package com.cocahonka.saltomaru.salt_block

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack


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
                val saltBlockItem = ItemStack(Material.WHITE_GLAZED_TERRACOTTA)
                val meta = saltBlockItem.itemMeta

                val displayName = Component.text(SaltBlock.DISPLAY_NAME)
                val lore = listOf(Component.text(SaltBlock.LORE).color(NamedTextColor.GRAY))

                meta.lore(lore)
                meta.displayName(displayName)
                saltBlockItem.itemMeta = meta

                event.block.world.dropItemNaturally(event.block.location, saltBlockItem)
            } else {
                val fortuneLevel = tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)
                val saltParticles = SaltBlock.getRandomSaltParticles(fortuneLevel)
                block.world.dropItemNaturally(
                    block.location,
                    ItemStack(Material.SUGAR, saltParticles)
                )
            }
        }
    }

}