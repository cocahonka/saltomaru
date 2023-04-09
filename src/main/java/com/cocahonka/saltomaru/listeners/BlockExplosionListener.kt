package com.cocahonka.saltomaru.listeners

import com.cocahonka.saltomaru.salt.block.SaltBlock
import com.cocahonka.saltomaru.salt.item.SaltPiece
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.inventory.ItemStack

class BlockExplosionListener : Listener {

    // TODO REMOVE HARDCODE
    private val saltPiece = SaltPiece()
    private val saltBlock = SaltBlock()

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        val brokenBlocks = event.blockList()
        for (block in brokenBlocks) {
            if (saltBlock.isValidBlock(block)) {
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
