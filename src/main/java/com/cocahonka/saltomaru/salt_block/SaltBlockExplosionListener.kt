package com.cocahonka.saltomaru.salt_block

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.EntityType
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
                    120, // Количество частиц
                    3.5, 3.5, 5.0, // Размер разброса частиц
                    0.5, // Скорость частиц
                    ItemStack(Material.SUGAR) // Тип частицы (частицы соли)
                )

                val sugarAmount = (1..3).random()
                val sugar = ItemStack(Material.SUGAR, sugarAmount)
                block.world.dropItemNaturally(block.location, sugar)

            }
        }

    }
}
