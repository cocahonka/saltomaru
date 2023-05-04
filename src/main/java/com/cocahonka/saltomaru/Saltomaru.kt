package com.cocahonka.saltomaru

import com.cocahonka.saltomaru.config.SaltomaruConfig
import com.cocahonka.saltomaru.database.SaltomaruDatabase
import com.cocahonka.saltomaru.events.cauldron.Cauldron
import com.cocahonka.saltomaru.events.salt.TreeBarkSaltEvent
import com.cocahonka.saltomaru.generators.SaltMountainGenerator
import com.cocahonka.saltomaru.salt.armor.SaltBoots
import com.cocahonka.saltomaru.salt.armor.SaltChestplate
import com.cocahonka.saltomaru.salt.block.SaltBlock
import com.cocahonka.saltomaru.salt.armor.SaltHelmet
import com.cocahonka.saltomaru.salt.armor.SaltLeggings
import com.cocahonka.saltomaru.salt.item.SaltPiece
import kotlinx.coroutines.*
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * Главный класс плагина Saltomaru, предоставляющий различные функции и элементы связанные с
 * миром соли в игре Minecraft, такие как: блоки соли, предметы соли и броню соли, а также
 * события и генераторы ландшафта.
 *
 * @property database экземпляр [SaltomaruDatabase] для взаимодействия с базой данных
 * @property job корневая задача для корутин, используемых в плагине
 * @property scope контекст выполнения корутин, используемых в плагине
 */
@Suppress("unused")
class Saltomaru : JavaPlugin() {

    private lateinit var database: SaltomaruDatabase
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Suppress("UNUSED_VARIABLE")
    override fun onEnable() {
        logger.info("\u001B[32m" + "Saltomaru by cocahonka!" + "\u001B[0m")

        database = SaltomaruDatabase.getInstance(this)
        startPeriodicSync()

        val plugin = this

        val saltPiece = SaltPiece()
        val saltBlock = SaltBlock(this, saltPiece).apply {
            registerItemRecipe()
            registerEvent(plugin)
        }

        val saltHelmet = SaltHelmet(this, saltPiece).apply {
            registerItemRecipe()
            registerEvent(plugin)
        }
        val saltLeggings = SaltLeggings(this, saltPiece).apply {
            registerItemRecipe()
            registerEvent(plugin)
        }
        val saltBoots = SaltBoots(this, saltPiece).apply {
            registerItemRecipe()
            registerEvent(plugin)
        }
        val saltChestplate = SaltChestplate(this, saltPiece).apply {
            registerItemRecipe()
            registerEvent(plugin)
        }

        val treeBarkEvent = TreeBarkSaltEvent(saltPiece).apply {
            registerEvent(plugin)
        }

        val generator = SaltMountainGenerator(saltBlock)
        val overworld = server.worlds[0]
        overworld.populators.add(generator)

    }

    override fun onDisable() {
        runBlocking {
            job.cancelAndJoin()
            logger.info("Stopping periodic sync")
        }
        database.close()
    }

    /**
     * Возвращает путь к файлу хранения на основе имени файла в папке данных плагина.
     * Если папка данных не существует, она будет создана.
     *
     * @param fileName имя файла для поиска или создания в папке данных плагина
     * @return [File] объект, представляющий путь к указанному файлу
     */
    fun getStoragePath(fileName: String): File {
        val dataFolder = dataFolder
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        return File(dataFolder, fileName)
    }

    /**
     * Запускает периодическую синхронизацию данных между кешем и базой данных в корутине.
     * Интервал синхронизации определяется в [SaltomaruConfig.Database.SYNC_PERIOD_MS].
     */
    private fun startPeriodicSync() {
        logger.info("Loading data from db...")
        Cauldron.synchronizeCachedWithDatabase()
        scope.launch {
            while (isActive) {
                delay(SaltomaruConfig.Database.SYNC_PERIOD_MS)
                logger.info("Synchronize...")
                Cauldron.synchronizeCachedWithDatabase()
            }
        }
    }
}
