package com.cocahonka.saltomaru

import org.bukkit.plugin.java.JavaPlugin

class Saltomaru : JavaPlugin() {
    override fun onEnable() {
        getLogger().info("Hello world!");
        server.pluginManager.registerEvents(SphereGenerator(), this)
        server.pluginManager.registerEvents(BlockBreakListener(),this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}