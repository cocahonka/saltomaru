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

    override fun onEnable() {
        getLogger().info("\u001B[32m" + "Saltomaru by cocahonka!" +  "\u001B[0m")

        val saltPiece = SaltPiece()
        val saltBlock = SaltBlock(this, saltPiece)
        val saltHelmet = SaltHelmet(this, saltPiece)
        val saltLeggings = SaltLeggings(this, saltPiece)

        craftingManager.addSaltomaruItem(saltHelmet)
        craftingManager.addSaltomaruItem(saltBlock)

        blockManager.addSaltomaruBlock(saltBlock)

        eventManager.addSaltomaruEvent(TreeBarkSaltEvent(saltPiece))
        eventManager.addSaltomaruEvent(CraftingListener(craftingManager))
        eventManager.addSaltomaruEvent(BlockBreakListener(blockManager))
        eventManager.addSaltomaruEvent(BlockExplosionListener(blockManager))
        eventManager.addSaltomaruEvent(BlockPlaceListener(blockManager))

        registerEvents()

        val generator = SaltMountainGenerator(saltBlock)
        val overworld = server.worlds[0]
        overworld.populators.add(generator)

    }

    private fun registerEvents(){
        for(event in eventManager.saltomaruEvents){
            server.pluginManager.registerEvents(event,this)
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}