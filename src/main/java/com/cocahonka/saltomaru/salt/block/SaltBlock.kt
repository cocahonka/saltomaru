package com.cocahonka.saltomaru.salt.block

import com.cocahonka.saltomaru.base.SaltomaruBlock
import com.cocahonka.saltomaru.salt.item.SaltPiece
import com.cocahonka.saltomaru.managers.SaltomaruBlockManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.inventory.ItemStack

class SaltBlock : SaltomaruBlock() {

    override val displayName = "Солевой блок"
    override val lore = "Salt helmet"
    override val loreComponent = Component.text(lore).color(NamedTextColor.GRAY)
    override val nameComponent = Component.text(displayName)
    override val material = Material.WHITE_GLAZED_TERRACOTTA
    override val customModelData = 1
    override val facing = BlockFace.NORTH

    // TODO: REMOVE HARDCODE
    private val saltPiece = SaltPiece()
    private val validTools = listOf(
        Material.NETHERITE_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.STONE_PICKAXE,
    )

    override fun getNewItemStack(amount: Int): ItemStack {
        val saltBlock = ItemStack(material, amount)
        val meta = saltBlock.itemMeta

        meta.lore(listOf(loreComponent))
        meta.displayName(nameComponent)
        meta.setCustomModelData(customModelData)

        saltBlock.itemMeta = meta

        return saltBlock
    }

    private fun getRandomSaltPiecesAmount(fortuneLevel: Int): Int {
        val baseAmount = (1..3).random()
        val extra = if (fortuneLevel > 0) (0..fortuneLevel).random() else 0
        return baseAmount + extra
    }

    private fun getBreakSound(): Sound {
        return Sound.BLOCK_BONE_BLOCK_BREAK
    }

    override fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (isValidBlock(block)) {
            event.isCancelled = true
            block.type = Material.AIR
            val player = event.player
            val tool = player.inventory.itemInMainHand

            block.world.playSound(block.location, getBreakSound(), 1.0f, 1.0f)

            if (!validTools.contains(tool.type)) return

            if (tool.containsEnchantment(Enchantment.SILK_TOUCH)) {
                val saltBlockItem = getNewItemStack()

                event.block.world.dropItemNaturally(event.block.location, saltBlockItem)
            } else {
                val fortuneLevel = tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)
                val saltPiecesAmount = getRandomSaltPiecesAmount(fortuneLevel)

                val saltPieceItem = saltPiece.getNewItemStack(saltPiecesAmount)

                block.world.dropItemNaturally(block.location, saltPieceItem)
            }
        }
    }

    override fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == material) {
            val item = event.itemInHand

            if (isValidItem(item)) {
                val blockData = createBlockData()
                event.block.blockData = blockData
            } else {
                val blockData = event.block.blockData as Directional
                if(blockData.facing == facing) {
                    blockData.facing = SaltomaruBlockManager.notUsedFace
                    event.block.blockData = blockData
                }
            }
        }
    }

    override fun onEntityExplode(event: EntityExplodeEvent) {
        val brokenBlocks = event.blockList()
        for (block in brokenBlocks) {
            if (isValidBlock(block)) {
                block.type = Material.AIR
                block.world.spawnParticle(
                    Particle.ITEM_CRACK,
                    0.5,
                    0.5,
                    0.5,
                    120,
                    3.5, 3.5, 5.0,
                    0.5,
                    ItemStack(Material.SUGAR)
                )

                val saltPiecesAmount = (1..3).random()
                val saltPieces = saltPiece.getNewItemStack(saltPiecesAmount)

                block.world.dropItemNaturally(block.location, saltPieces)

            }
        }

    }
}