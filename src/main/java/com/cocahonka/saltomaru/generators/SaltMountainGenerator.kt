package com.cocahonka.saltomaru.generators

import com.cocahonka.saltomaru.events.salt.TreeBarkSaltEvent.Chances
import com.cocahonka.saltomaru.salt.block.SaltBlock
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Biome
import org.bukkit.block.data.BlockData
import org.bukkit.generator.BlockPopulator
import org.bukkit.generator.LimitedRegion
import org.bukkit.generator.WorldInfo
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Класс отвественный за генерацию солевых гор (новых структур)
 * @param saltBlock объект [SaltBlock] солевой блок
 * @property Chances содержить статические поля вероятности событий
 * @property CHANCE_TO_SPAWN вероятность спавна структуры
 * @property MEDIUM_MOUNTAIN_CHANCE вероятность спавна структуры среднего размера
 * @property LARGE_MOUNTAIN_CHANCE вероятность спавна структуры большого размера
 * @property CALCITE_CHANCE вероятность генерации вкраплений из [Material.CALCITE] в структуре
 * @property TILT_FACTOR коэффициент наклона структуры
 * @property MAX_HEIGHT_SPAWN максимальная высота спавна структуры
 * @property MIN_HEIGHT_SPAWN минимальная высота спавна структуры
 * @property MAX_HEIGHT_DIFFERENCE максимальное различие высот на поверхности пригодное для спавна
 * @property validGroundBiomes массив надзменых биомов в которых может заспавниться структура
 * @property validOceanBiomes массив океанских биомов в которых может заспавниться структура
 * @property allValidBiomes массив включающий себя [validGroundBiomes] и [validOceanBiomes]
 */
class SaltMountainGenerator(private val saltBlock: SaltBlock) : BlockPopulator() {

    companion object Chances {
        private const val CHANCE_TO_SPAWN = 0.08
        private const val MEDIUM_MOUNTAIN_CHANCE = 0.3
        private const val LARGE_MOUNTAIN_CHANCE = 0.1
        private const val CALCITE_CHANCE = 0.4
        private const val TILT_FACTOR = 0.4
        private const val MAX_HEIGHT_SPAWN = 200
        private const val MIN_HEIGHT_SPAWN = 30
        private const val MAX_HEIGHT_DIFFERENCE = 4
    }

    private val validGroundBiomes = listOf(
        Biome.BEACH,
        Biome.SNOWY_BEACH,
        Biome.SNOWY_SLOPES,
        Biome.WINDSWEPT_HILLS,
    )

    private val validOceanBiomes = listOf(
        Biome.WARM_OCEAN,
        Biome.LUKEWARM_OCEAN,
        Biome.DEEP_LUKEWARM_OCEAN,
    )

    private val allValidBiomes = validGroundBiomes + validOceanBiomes

    override fun populate(
        worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int, limitedRegion: LimitedRegion,
    ) {
        if (random.nextDouble() < CHANCE_TO_SPAWN) {

            val location = getStructureValidLocation(chunkX, chunkZ, limitedRegion)
            if (location == null || !limitedRegion.isInRegion(location)) {
                return
            }

            val currentBiome = limitedRegion.getBiome(location)
            if (!allValidBiomes.contains(currentBiome)) {
                return
            }

            val startBlockData = limitedRegion.getBlockData(location)

            if (startBlockData.material != Material.WATER) {
                val randomDouble = random.nextDouble()
                val mountainSize = when {
                    (randomDouble < LARGE_MOUNTAIN_CHANCE) && validOceanBiomes.contains(currentBiome) -> 3
                    randomDouble < MEDIUM_MOUNTAIN_CHANCE -> 2
                    else -> 1
                }

                generateSaltMountain(limitedRegion, location, mountainSize, random)
            }
        }
    }

    /**
     * Функция получения валидной локации для спавна структуры
     * @param chunkX чанк по счету по оси X
     * @param chunkZ чанк по счету по оси Z
     * @param limitedRegion регион сервера пригодный для спавна структуры (предоставляется из [populate])
     * @return [Location] если регион сервера позволяет спавн и если нашлось валидное место, иначе null
     */
    private fun getStructureValidLocation(
        chunkX: Int,
        chunkZ: Int,
        limitedRegion: LimitedRegion,
    ): Location? {
        var highestY = MIN_HEIGHT_SPAWN

        for (x in 0..15) {
            for (z in 0..15) {
                var currentY = MAX_HEIGHT_SPAWN
                while (currentY >= MIN_HEIGHT_SPAWN) {
                    val currentBlock = limitedRegion.getBlockData(chunkX * 16 + x, currentY, chunkZ * 16 + z)
                    if (currentBlock.material.isSolid && !currentBlock.material.isBurnable) {
                        break
                    }
                    currentY--
                }

                if (currentY > highestY) {
                    highestY = currentY
                }

                if (highestY - currentY <= MAX_HEIGHT_DIFFERENCE) {
                    val yAverage = ((highestY + currentY) / 2) - 4
                    return Location(
                        null,
                        (chunkX * 16 + x).toDouble(),
                        yAverage.toDouble(),
                        (chunkZ * 16 + z).toDouble(),
                    )
                }
            }
        }
        return null
    }

    /**
     * Функция генерации солевой горы по частям (верхняя часть и нижняя)
     * @param limitedRegion регион сервера пригодный для спавна структуры (предоставляется из [populate])
     * @param location место для генерации
     * @param mountainSize коэффициент размера горы в зависимости от [Chances]
     * @param random генератор рандомных вероятностей
     */
    private fun generateSaltMountain(
        limitedRegion: LimitedRegion,
        location: Location,
        mountainSize: Int,
        random: Random,
    ) {
        val height = 3 * mountainSize + random.nextInt(3) + 4
        val baseRadius = 2 * mountainSize + random.nextInt(2) + 1

        val tiltDirectionX = random.nextDouble() * 2 - 1
        val tiltDirectionZ = random.nextDouble() * 2 - 1

        // Генерация нижней части горы
        for (y in -baseRadius  until 0) {
            val currentRadius = (baseRadius * (1 - (y.toDouble() / baseRadius).pow(2))).toInt()
            generateMountainLayer(limitedRegion, location, currentRadius, y, tiltDirectionX, tiltDirectionZ, random)
        }

        // Генерация верхней части горы
        for (y in 0 until height) {
            val currentRadius = baseRadius - (y.toDouble() / height * baseRadius).toInt()
            generateMountainLayer(limitedRegion, location, currentRadius, y, tiltDirectionX, tiltDirectionZ, random)
        }
    }

    /**
     * Функция генерирующая часть горы
     * @param limitedRegion регион сервера пригодный для спавна структуры (предоставляется из [populate])
     * @param location место для генерации
     * @param currentRadius радиус основания горы
     * @param y высота с которой начнется генерация
     * @param tiltDirectionX коэффициент наклона по оси X
     * @param tiltDirectionZ коэффициент наклона по оси Z
     * @param random генератор рандомных вероятностей
     */
    private fun generateMountainLayer(
        limitedRegion: LimitedRegion,
        location: Location,
        currentRadius: Int,
        y: Int,
        tiltDirectionX: Double,
        tiltDirectionZ: Double,
        random: Random
    ) {
        for (x in -currentRadius..currentRadius) {
            for (z in -currentRadius..currentRadius) {
                val distance = sqrt((x * x + z * z).toDouble())
                if (distance <= currentRadius + random.nextDouble()) {
                    val tiltedX = x + (tiltDirectionX * y * TILT_FACTOR).toInt()
                    val tiltedZ = z + (tiltDirectionZ * y * TILT_FACTOR).toInt()
                    val newLocation = Location(
                        null,
                        location.x + tiltedX,
                        location.y + y,
                        location.z + tiltedZ,
                    )
                    if (limitedRegion.isInRegion(newLocation)) {
                        val blockData = limitedRegion.getBlockData(newLocation)
                        if (
                            blockData.material == Material.AIR ||
                            blockData.material == Material.WATER ||
                            blockData.material == Material.SNOW
                        ) {
                            val mountainBlockData = getRandomBlockData(random)
                            limitedRegion.setBlockData(newLocation, mountainBlockData)
                        }
                    }
                }
            }
        }
    }

    /**
     * Фукнция для получения текущего блока солевой горы
     *
     * @param random генератор случайных вероятностей
     * @return [BlockData] блока в зависимости от [CALCITE_CHANCE]
     */
    private fun getRandomBlockData(random: Random): BlockData {
        return if (random.nextDouble() < CALCITE_CHANCE) {
            Material.CALCITE.createBlockData()
        } else {
            saltBlock.createBlockData()
        }
    }


}
