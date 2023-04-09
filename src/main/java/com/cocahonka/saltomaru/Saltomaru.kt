package com.cocahonka.saltomaru

import com.cocahonka.saltomaru.managers.SaltomaruBlockManager
import com.cocahonka.saltomaru.managers.SaltomaruCraftingManager
import com.cocahonka.saltomaru.generators.SaltMountainGenerator
import com.cocahonka.saltomaru.listeners.*
import com.cocahonka.saltomaru.salt.block.SaltBlock
import com.cocahonka.saltomaru.salt.item.SaltHelmet
import org.bukkit.plugin.java.JavaPlugin

class Saltomaru : JavaPlugin() {

    private val craftingManager = SaltomaruCraftingManager()
    private val blockManager = SaltomaruBlockManager()

    override fun onEnable() {
        getLogger().info("\u001B[32m" + "Saltomaru by cocahonka!" +  "\u001B[0m")

        craftingManager.addSaltomaruItem(SaltHelmet(this))

        val saltBlock = SaltBlock()
        blockManager.addSaltomaruBlock(saltBlock)

        server.pluginManager.registerEvents(CraftingListener(craftingManager),this)
        server.pluginManager.registerEvents(BlockBreakListener(blockManager),this)
        server.pluginManager.registerEvents(BlockExplosionListener(),this)
        server.pluginManager.registerEvents(BlockPlaceListener(blockManager),this)

        val generator = SaltMountainGenerator(saltBlock)
        val overworld = server.worlds[0]
        overworld.populators.add(generator)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}