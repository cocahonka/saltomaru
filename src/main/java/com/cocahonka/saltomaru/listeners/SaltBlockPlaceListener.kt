package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.salt.SaltBlock
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class SaltBlockPlaceListener : Listener {

    // private val availableFaces = listOf(BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH)
    private val defaultBlockFace = BlockFace.EAST

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == SaltBlock.material) {
            val item = event.itemInHand

            if (SaltBlock.isSaltBlockItem(item)) {
                val blockData = SaltBlock.createBlockData()
                event.block.blockData = blockData
            } else {
                val blockData = event.block.blockData as Directional
                if(blockData.facing == SaltBlock.facing) {
                    blockData.facing = defaultBlockFace
                    event.block.blockData = blockData
                }
            }
        }
    }
}
