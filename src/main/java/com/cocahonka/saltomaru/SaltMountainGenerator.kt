package com.cocahonka.saltomaru

import org.bukkit.Chunk
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import org.bukkit.generator.BlockPopulator
import java.util.Random
import kotlin.math.sqrt

class SaltMountainGenerator : BlockPopulator() {

    override fun populate(world: World, random: Random, chunk: Chunk) {
        if (random.nextDouble() < CHANCE_TO_SPAWN) {
            val x = random.nextInt(16) + chunk.x * 16
            val z = random.nextInt(16) + chunk.z * 16
            val y = world.getHighestBlockYAt(x, z) - 1 - 4

            val startBlock = world.getBlockAt(x, y, z)

            if (startBlock.type != Material.WATER) {
                val mountainSize = when {
                    random.nextDouble() < LARGE_MOUNTAIN_CHANCE -> 3
                    random.nextDouble() < MEDIUM_MOUNTAIN_CHANCE -> 2
                    else -> 1
                }

                generateSaltMountain(startBlock, mountainSize, random)
            }
        }
    }

    private fun generateSaltMountain(startBlock: org.bukkit.block.Block, mountainSize: Int, random: Random) {
        val height = 6 * mountainSize + random.nextInt(3) + 4
        val baseRadius = 2 * mountainSize + random.nextInt(2) + 1

        val tiltDirectionX = random.nextDouble() * 2 - 1
        val tiltDirectionZ = random.nextDouble() * 2 - 1

        for (y in 0 until height) {
            val currentRadius = baseRadius - (y.toDouble() / height * baseRadius).toInt()
            for (x in -currentRadius..currentRadius) {
                for (z in -currentRadius..currentRadius) {
                    val distance = sqrt((x * x + z * z).toDouble())
                    if (distance <= currentRadius + random.nextDouble()) {
                        val tiltedX = x + (tiltDirectionX * y * TILT_FACTOR).toInt()
                        val tiltedZ = z + (tiltDirectionZ * y * TILT_FACTOR).toInt()
                        val block = startBlock.getRelative(tiltedX, y, tiltedZ)
//                        val belowBlock = startBlock.getRelative(tiltedX, y - 1, tiltedZ)

                        if (block.type == Material.AIR || block.type == Material.WATER) {
                            val blockData = getRandomBlockData(random)
                            if (blockData is Directional) {
                                blockData.facing = BlockFace.NORTH
                            }
                            block.blockData = blockData
                        }

//                        if (belowBlock.type == Material.AIR) {
//                            belowBlock.type = Material.MOSS_BLOCK
//                        }
                    }
                }
            }
        }
    }

    private fun getRandomBlockData(random: Random): BlockData {
        return if (random.nextDouble() < QUARTZ_CHANCE) {
            Material.QUARTZ_BLOCK.createBlockData()
        } else {
            Material.WHITE_GLAZED_TERRACOTTA.createBlockData()
        }
    }

    companion object {
        private const val CHANCE_TO_SPAWN = 0.03
        private const val MEDIUM_MOUNTAIN_CHANCE = 0.3
        private const val LARGE_MOUNTAIN_CHANCE = 0.1
        private const val QUARTZ_CHANCE = 0.4
        private const val TILT_FACTOR = 0.4
    }
}
