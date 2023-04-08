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
        const val customModelData = 1
        val loreComponent = Component.text(LORE).color(NamedTextColor.GRAY)
        val nameComponent = Component.text(DISPLAY_NAME)
        val material = Material.WHITE_GLAZED_TERRACOTTA
        val facing = BlockFace.NORTH

        fun isSaltBlock(block: Block): Boolean {
            if (block.type != material) {
                return false
            }

            val blockData = block.blockData
            return blockData is Directional && blockData.facing == facing
        }

        fun isSaltBlockItem(item: ItemStack): Boolean {
            val meta = item.itemMeta
            return meta.hasLore() && meta.lore()?.contains(loreComponent) ?: false
        }

        fun getRandomSaltPiecesAmount(fortuneLevel: Int): Int {
            val baseAmount = (1..3).random()
            val extra = if (fortuneLevel > 0) (0..fortuneLevel).random() else 0
            return baseAmount + extra
        }

        fun getBreakSound(): Sound {
            return Sound.BLOCK_BONE_BLOCK_BREAK
        }

        fun getNewItemStack(amount: Int = 1): ItemStack {
            val saltBlock = ItemStack(material, amount)
            val meta = saltBlock.itemMeta

            meta.lore(listOf(loreComponent))
            meta.displayName(nameComponent)
            meta.setCustomModelData(customModelData)

            saltBlock.itemMeta = meta

            return saltBlock
        }

        fun createBlockData() : Directional {
            val blockData = material.createBlockData() as Directional
            blockData.facing = facing
            return blockData
        }
    }
}