package com.cocahonka.saltomaru.base

import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.block.data.BlockData

/**
 * Интерфейс кастомного блока, базирующийся на ванильных блоках из класса [Directional] (имеющих блок стейт facing)
 * @property facing направление блока (блок стейт)
 * @property notUsedFace статическое поле отвечающее за блок стейт который не должен быть
 * занят кастомными блоками
 */
interface SaltomaruBlock : SaltomaruItem {
    companion object {
        val notUsedFace = BlockFace.EAST
    }

    val facing: BlockFace

    /**
     * Функция опознания кастомного блока среди ванильных
     *
     * Опознание происходит по [facing] и [material]
     * @param block блок для опознания
     * @return возращает true если блок кастомный
     */
    fun isValidBlock(block: Block): Boolean {
        if (block.type != material) return false
        val blockData = block.blockData
        return blockData is Directional && blockData.facing == facing
    }

    /**
     * Функция создающая новый объект [Directional] (подкласс [BlockData])
     * @return новый объект [BlockData] с установленным [facing]
     */
    fun createBlockData(): Directional {
        val blockData = material.createBlockData() as Directional
        blockData.facing = facing
        return blockData
    }
}
