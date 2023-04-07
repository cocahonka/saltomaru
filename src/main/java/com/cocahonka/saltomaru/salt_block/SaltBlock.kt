package com.cocahonka.saltomaru.salt_block

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional

class SaltBlock {
    companion object {
        const val DISPLAY_NAME = "Солевой блок"
        const val LORE = "Salt block"
        internal fun isSaltBlock(block: Block): Boolean {
            if (block.type != Material.WHITE_GLAZED_TERRACOTTA) {
                return false
            }

            val blockData = block.blockData
            return blockData is Directional && blockData.facing == BlockFace.NORTH
        }
        internal fun getRandomSaltParticles(fortuneLevel: Int): Int {
            val baseAmount = (1..3).random()
            val extra = if (fortuneLevel > 0) (0..fortuneLevel).random() else 0
            return baseAmount + extra
        }

        internal fun getBreakSound(): Sound {
            return Sound.BLOCK_BONE_BLOCK_BREAK
        }
    }


}