package com.cocahonka.saltomaru.base

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional

interface SaltomaruBlock : SaltomaruItem {
    companion object {
        val notUsedFace = BlockFace.EAST
    }

    val facing: BlockFace
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
