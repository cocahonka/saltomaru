package com.cocahonka.saltomaru

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkPopulateEvent
import java.util.*


class SphereGenerator : Listener {
    private val random = Random()
    @EventHandler
    fun onChunkPopulate(event: ChunkPopulateEvent) {
        val chunk = event.chunk
        val world = chunk.world
        if (random.nextDouble() < 0.15) { // 15% шанс на генерацию шара в чанке
            val sphereX = chunk.x * 16 + random.nextInt(16)
            val sphereZ = chunk.z * 16 + random.nextInt(16)
            val sphereY = world.getHighestBlockYAt(sphereX, sphereZ)
            generateSphere(world, sphereX, sphereY, sphereZ, Material.IRON_ORE)
        }
    }

    private fun generateSphere(world: World, centerX: Int, centerY: Int, centerZ: Int, material: Material) {
        val radius = random.nextInt(4) + 2 // Радиус шара от 2 до 5 блоков
        val squaredRadius = Math.pow(radius.toDouble(), 2.0)
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    if (Math.pow(x.toDouble(), 2.0) + Math.pow(y.toDouble(), 2.0) + Math.pow(
                            z.toDouble(),
                            2.0
                        ) <= squaredRadius
                    ) {
                        val block = world.getBlockAt(centerX + x, centerY + y, centerZ + z)
                        block.setType(material, false)
                    }
                }
            }
        }
    }
}
