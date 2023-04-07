package com.cocahonka.saltomaru.salt_block

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit.getLogger
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class SaltBlockPlaceListener : Listener {

    private val availableFaces = listOf(BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH)

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == Material.WHITE_GLAZED_TERRACOTTA) {
            val item = event.itemInHand
            val lore = item.itemMeta.lore()

            if (lore != null && lore.contains(Component.text(SaltBlock.LORE).color(NamedTextColor.GRAY))) {
                val blockData = event.block.blockData as Directional
                blockData.facing = BlockFace.NORTH
                event.block.blockData = blockData
            } else {
                val blockData = event.block.blockData as Directional
                if(blockData.facing == BlockFace.NORTH) {
                    blockData.facing = availableFaces.random()
                    event.block.blockData = blockData
                }
            }
        }
    }
}
