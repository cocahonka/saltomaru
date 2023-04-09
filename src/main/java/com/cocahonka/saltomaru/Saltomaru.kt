package com.cocahonka.saltomaru

import com.cocahonka.saltomaru.crafting.SaltCrafting
import com.cocahonka.saltomaru.generators.SaltMountainGenerator
import com.cocahonka.saltomaru.listeners.*
import org.bukkit.plugin.java.JavaPlugin

class Saltomaru : JavaPlugin() {

    private val craftingManager = SaltCrafting(this)

    override fun onEnable() {
        getLogger().info("\u001B[32m" + "Saltomaru by cocahonka!" +  "\u001B[0m")


        server.pluginManager.registerEvents(SaltCraftingListener(craftingManager),this)
        server.pluginManager.registerEvents(SaltBlockBreakListener(),this)
        server.pluginManager.registerEvents(SaltBlockExplosionListener(),this)
        server.pluginManager.registerEvents(SaltBlockPlaceListener(),this)

        val generator = SaltMountainGenerator()
        val overworld = server.worlds[0]
        overworld.populators.add(generator)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}