package com.cocahonka.saltomaru

import org.bukkit.WorldCreator
import org.bukkit.generator.ChunkGenerator
import org.bukkit.plugin.java.JavaPlugin

class Saltomaru : JavaPlugin() {
    override fun onEnable() {
        getLogger().info("Saltomaru by cocahonka!");
        server.pluginManager.registerEvents(SaltOreListener(),this)

        val generator = SaltMountainGenerator()
        server.worlds.forEach { world -> world.populators.add(generator) }
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}