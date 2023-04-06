package com.cocahonka.saltomaru

import org.bukkit.plugin.java.JavaPlugin

class Saltomaru : JavaPlugin() {
    override fun onEnable() {
        getLogger().info("Saltomaru by cocahonka!");
        server.pluginManager.registerEvents(SaltOreListener(),this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}