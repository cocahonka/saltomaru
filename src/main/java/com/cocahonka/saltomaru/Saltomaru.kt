package com.cocahonka.saltomaru

import com.cocahonka.saltomaru.generators.SaltMountainGenerator
import com.cocahonka.saltomaru.salt_block.SaltBlockBreakListener
import com.cocahonka.saltomaru.salt_block.SaltBlockExplosionListener
import com.cocahonka.saltomaru.salt_block.SaltBlockPlaceListener
import org.bukkit.plugin.java.JavaPlugin

class Saltomaru : JavaPlugin() {
    override fun onEnable() {
        getLogger().info("Saltomaru by cocahonka!");
        server.pluginManager.registerEvents(SaltBlockBreakListener(),this)
        server.pluginManager.registerEvents(SaltBlockExplosionListener(),this)
        server.pluginManager.registerEvents(SaltBlockPlaceListener(),this)

        val generator = SaltMountainGenerator()
        server.worlds.forEach { world -> world.populators.add(generator) }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}