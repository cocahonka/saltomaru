package com.cocahonka.saltomaru.base

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent

interface SaltomaruBlock : SaltomaruItem {
    val facing: BlockFace

    fun onBlockBreak(event: BlockBreakEvent)

    fun onBlockPlace(event: BlockPlaceEvent)

    fun onEntityExplode(event: EntityExplodeEvent)

    fun isValidBlock(block: Block): Boolean {
        if (block.type != material) {
            return false
        }

        val blockData = block.blockData
        return blockData is Directional && blockData.facing == facing
    }

    fun createBlockData(): Directional {
        val blockData = material.createBlockData() as Directional
        blockData.facing = facing
        return blockData
    }
}
