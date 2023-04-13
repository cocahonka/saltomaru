package com.cocahonka.saltomaru

import com.cocahonka.saltomaru.events.salt.TreeBarkSaltEvent
import com.cocahonka.saltomaru.managers.SaltomaruBlockManager
import com.cocahonka.saltomaru.managers.SaltomaruCraftingManager
import com.cocahonka.saltomaru.generators.SaltMountainGenerator
import com.cocahonka.saltomaru.listeners.*
import com.cocahonka.saltomaru.managers.SaltomaruEventManager
import com.cocahonka.saltomaru.salt.block.SaltBlock
import com.cocahonka.saltomaru.salt.armor.SaltHelmet
import com.cocahonka.saltomaru.salt.armor.SaltLeggings
import com.cocahonka.saltomaru.salt.item.SaltPiece
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Saltomaru : JavaPlugin() {

    private val craftingManager = SaltomaruCraftingManager()
    private val blockManager = SaltomaruBlockManager()
    private val eventManager = SaltomaruEventManager()

    @Suppress("UNUSED_VARIABLE")
    override fun onEnable() {
        getLogger().info("\u001B[32m" + "Saltomaru by cocahonka!" + "\u001B[0m")

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

        val treeBark = TreeBarkSaltEvent(saltPiece).apply {
            registerEvent(plugin)
        }

        val generator = SaltMountainGenerator(saltBlock)
        val overworld = server.worlds[0]
        overworld.populators.add(generator)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
