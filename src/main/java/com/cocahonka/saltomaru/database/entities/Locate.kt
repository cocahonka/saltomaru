package com.cocahonka.saltomaru.database.entities

import com.cocahonka.saltomaru.config.SaltomaruConfig.Minecraft as Config

data class Locate(
    val worldId: Int,
    val x: Int,
    val y: Int,
    val z: Int,
) {
    init {
        require(worldId in Config.WORLD_IDS)
        { "worldId must be between in ${Config.WORLD_IDS}" }

        require(x in Config.MIN_X..Config.MAX_X)
        { "x must be between ${Config.MIN_X}..${Config.MAX_X}" }

        require(z in Config.MIN_Z..Config.MAX_X)
        { "z must be between ${Config.MIN_Z}..${Config.MAX_Z}" }

        when (worldId) {
            Config.OVERWORLD_ID ->
                require(y in Config.MIN_OVERWORLD_Y..Config.MAX_OVERWORLD_Y)
                { "y in overworld must be between ${Config.MIN_OVERWORLD_Y}..${Config.MAX_OVERWORLD_Y}" }

            Config.NETHER_ID ->
                require(y in Config.MIN_NETHER_Y..Config.MAX_NETHER_Y)
                { "y in nether must be between ${Config.MIN_NETHER_Y}..${Config.MAX_NETHER_Y}" }

            Config.END_ID ->
                require(y in Config.MIN_END_Y..Config.MAX_END_Y)
                { "y in end must be between ${Config.MIN_END_Y}..${Config.MAX_END_Y}" }
        }
    }
}

