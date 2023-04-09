package com.cocahonka.saltomaru.managers

import com.cocahonka.saltomaru.base.SaltomaruBlock
import com.cocahonka.saltomaru.base.SaltomaruItemCraftable
import com.google.common.collect.ImmutableList
import org.bukkit.block.BlockFace

class SaltomaruBlockManager {

    companion object {
        val notUsedFace = BlockFace.EAST
    }

    private val blocks = mutableListOf<SaltomaruBlock>()
    val saltomaruBlocks: ImmutableList<SaltomaruBlock>
        get() = ImmutableList.copyOf(blocks)

    fun addSaltomaruBlock(block: SaltomaruBlock){
        blocks.add(block)
    }
}