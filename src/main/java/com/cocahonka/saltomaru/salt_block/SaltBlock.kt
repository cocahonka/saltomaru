package com.cocahonka.saltomaru.salt_block

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.inventory.ItemStack

class SaltBlock {
    companion object {
        const val DISPLAY_NAME = "Солевой блок"
        const val LORE = "Salt block"
        val loreComponent = Component.text(LORE).color(NamedTextColor.GRAY)
        val nameComponent = Component.text(DISPLAY_NAME)

        fun isSaltBlock(block: Block): Boolean {
            if (block.type != Material.WHITE_GLAZED_TERRACOTTA) {
                return false
            }

            val blockData = block.blockData
            return blockData is Directional && blockData.facing == BlockFace.NORTH
        }

        fun getRandomSaltParticles(fortuneLevel: Int): Int {
            val baseAmount = (1..3).random()
            val extra = if (fortuneLevel > 0) (0..fortuneLevel).random() else 0
            return baseAmount + extra
        }

        fun getBreakSound(): Sound {
            return Sound.BLOCK_BONE_BLOCK_BREAK
        }

        fun getNewItemStack(n: Int = 1): ItemStack {
            val saltBlock = ItemStack(Material.WHITE_GLAZED_TERRACOTTA, n)
            val meta = saltBlock.itemMeta

            meta.lore(listOf(loreComponent))
            meta.displayName(nameComponent)

            saltBlock.itemMeta = meta

            return saltBlock
        }

        fun createBlockData() : Directional {
            val blockData = Material.WHITE_GLAZED_TERRACOTTA.createBlockData() as Directional
            blockData.facing = BlockFace.NORTH
            return blockData
        }
    }
}