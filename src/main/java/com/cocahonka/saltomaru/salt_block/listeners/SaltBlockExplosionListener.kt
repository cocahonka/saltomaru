package com.cocahonka.saltomaru.salt_block.listeners

import com.cocahonka.saltomaru.salt_block.SaltBlock
import com.cocahonka.saltomaru.salt_block.SaltPiece
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.inventory.ItemStack

class SaltBlockExplosionListener : Listener {

    @EventHandler
    fun onEntityExplode(event: EntityExplodeEvent) {
        val brokenBlocks = event.blockList()
        for (block in brokenBlocks) {
            if (SaltBlock.isSaltBlock(block)) {
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
                val saltPieces = SaltPiece.getNewItemStack(saltPiecesAmount)

                block.world.dropItemNaturally(block.location, saltPieces)

            }
        }

    }
}
