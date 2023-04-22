package com.cocahonka.saltomaru

import com.cocahonka.saltomaru.database.SaltomaruDatabase
import com.cocahonka.saltomaru.events.salt.TreeBarkSaltEvent
import com.cocahonka.saltomaru.generators.SaltMountainGenerator
import com.cocahonka.saltomaru.salt.armor.SaltBoots
import com.cocahonka.saltomaru.salt.armor.SaltChestplate
import com.cocahonka.saltomaru.salt.block.SaltBlock
import com.cocahonka.saltomaru.salt.armor.SaltHelmet
import com.cocahonka.saltomaru.salt.armor.SaltLeggings
import com.cocahonka.saltomaru.salt.item.SaltPiece
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("unused")
class Saltomaru : JavaPlugin() {

    private lateinit var database: SaltomaruDatabase
    @Suppress("UNUSED_VARIABLE")
    override fun onEnable() {
        logger.info("\u001B[32m" + "Saltomaru by cocahonka!" + "\u001B[0m")

        database = SaltomaruDatabase.getInstance(this)

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

        val treeBark = TreeBarkSaltEvent(saltPiece).apply {
            registerEvent(plugin)
        }

        val generator = SaltMountainGenerator(saltBlock)
        val overworld = server.worlds[0]
        overworld.populators.add(generator)

    }

    override fun onDisable() {
        database.close()
    }

    fun getStoragePath(fileName: String): File {
        val dataFolder = dataFolder
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }
        return File(dataFolder, fileName)
    }
}
