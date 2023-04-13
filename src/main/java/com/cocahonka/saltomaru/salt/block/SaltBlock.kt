package com.cocahonka.saltomaru.salt.block

import com.cocahonka.saltomaru.base.SaltomaruBlock
import com.cocahonka.saltomaru.base.SaltomaruBlockEvent
import com.cocahonka.saltomaru.base.SaltomaruItemCraftable
import com.cocahonka.saltomaru.salt.item.SaltPiece
import com.cocahonka.saltomaru.utils.SaltomaruCraftingUtils
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.Plugin

class SaltBlock(plugin: Plugin, private val saltPiece: SaltPiece) :
    SaltomaruBlock,
    SaltomaruBlockEvent,
    SaltomaruItemCraftable {

    override val displayName = "Солевой блок"
    override val lore = "Salt block"
    override val loreComponent = Component.text(lore).color(NamedTextColor.GRAY)
    override val nameComponent = Component.text(displayName)
    override val material = Material.WHITE_GLAZED_TERRACOTTA
    override val customModelData = 1
    override val facing = BlockFace.NORTH
    override val recipeKey = NamespacedKey(plugin, "salt_block")

    private val validTools = listOf(
        Material.NETHERITE_PICKAXE,
        Material.DIAMOND_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.STONE_PICKAXE,
    )

    override fun registerItemRecipe(): Boolean {
        val saltBlock = getNewItemStack()

        val recipe = ShapedRecipe(recipeKey, saltBlock)
        recipe.shape("XXX", "XXX", "XXX")
        recipe.setIngredient('X', saltPiece.material)

        return Bukkit.addRecipe(recipe)
    }

    override fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        val recipe = event.recipe
        val inventory = event.inventory
        val player = event.viewers[0] as Player

        if (recipe is Keyed && recipe.key == recipeKey) {
            if (SaltomaruCraftingUtils.isValidMatrix(inventory.matrix, saltPiece::isValidItem)) {
                inventory.result = getNewItemStack()
            } else {
                inventory.result = null
                SaltomaruCraftingUtils.retrievePlayerCraft(inventory, player)
            }

        }
    }

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
        val validFortuneLevel = if (fortuneLevel > 3) 3 else fortuneLevel
        val extra = if (validFortuneLevel > 0) (0..validFortuneLevel * 2).random() else 0
        return baseAmount + extra
    }

    private fun getBreakSound(): Sound {
        return Sound.BLOCK_BONE_BLOCK_BREAK
    }

    @EventHandler
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

    @EventHandler
    override fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == material) {
            val item = event.itemInHand

            if (isValidItem(item)) {
                val blockData = createBlockData()
                event.block.blockData = blockData
            } else {
                val blockData = event.block.blockData as Directional
                if (blockData.facing == facing) {
                    blockData.facing = SaltomaruBlock.notUsedFace
                    event.block.blockData = blockData
                }
            }
        }
    }

    @EventHandler
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