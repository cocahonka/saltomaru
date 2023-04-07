package com.cocahonka.saltomaru

import com.cocahonka.saltomaru.generators.SaltMountainGenerator
import com.cocahonka.saltomaru.salt_block.SaltHelmet
import com.cocahonka.saltomaru.salt_block.listeners.*
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

class Saltomaru : JavaPlugin() {

    private val saltHelmetKey = NamespacedKey(this, "salt_helmet")
    override fun onEnable() {
        getLogger().info("\u001B[32m" + "Saltomaru by cocahonka!" +  "\u001B[0m")

        SaltHelmet(saltHelmetKey).registerSaltHelmetRecipe()

        server.pluginManager.registerEvents(SaltCraftingListener(saltHelmetKey),this)
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