package logic

import App
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.experimental.and


class Logic(private val plugin: App) {
    fun replacerTreeToFence(tree: Material): Material? {
        return when (tree) {
            Material.OAK_LOG -> Material.OAK_FENCE
            Material.BIRCH_LOG -> Material.BIRCH_FENCE
            Material.SPRUCE_LOG -> Material.SPRUCE_FENCE
            Material.ACACIA_LOG -> Material.ACACIA_FENCE
            Material.DARK_OAK_LOG -> Material.DARK_OAK_FENCE
            Material.JUNGLE_LOG -> Material.JUNGLE_FENCE
            else -> null
        }
    }
    fun giveFoundBlock(centerBlock: Block): Block? {
        return getRandomTreeBlock(centerBlock)
    }
    fun replacerTreeToFenceInRadius(foundBlock: Block) : Boolean{
        val fenceType = when (foundBlock.type) {
            Material.OAK_LOG -> Material.OAK_FENCE
            Material.BIRCH_LOG -> Material.BIRCH_FENCE
            Material.SPRUCE_LOG -> Material.SPRUCE_FENCE
            Material.ACACIA_LOG -> Material.ACACIA_FENCE
            Material.DARK_OAK_LOG -> Material.DARK_OAK_FENCE
            Material.JUNGLE_LOG -> Material.JUNGLE_FENCE
            else -> null
        }
        if (fenceType != null) {
            foundBlock.type = fenceType
            return true
        }
    return false
    }
    fun replacerFenceToTree(block: Block, newMaterial: Material, ticks: Int) {
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            block.type = newMaterial
        }, ticks.toLong())
    }
    fun checkTypeTreeBlock(tree: Material) : Boolean {
        val treeMaterials = getTreeMaterials()
        return treeMaterials.contains(tree)
    }
    fun checkTypeFenceBlock(fence: Material) : Boolean{
        val fenceMaterial = getFenceMaterials()
        return fenceMaterial.contains(fence)
    }
    fun checkTypeToolsBreak(tools: Material) : Boolean{
        val toolsMaterial = getToolsList()
        return toolsMaterial.contains(tools)
    }
    fun giveTreeToPlayer(player: Player, tree: Material){
        player.inventory.addItem(ItemStack(tree))
    }
    private fun findTreeInCube(centerBlock: Block): List<Block> {
        val foundBlocks = mutableListOf<Block>()
        val targetMaterials = getTreeMaterials()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val block = centerBlock.getRelative(x, y, z)
                    if (targetMaterials.contains(block.type) && block.type != Material.AIR) {
                        val typeLog = convertFenceToLog(centerBlock)
                        if (block.type == typeLog) {
                            foundBlocks.add(block)
                        }
                    }
                }
            }
        }
        return foundBlocks
    }
    private fun convertFenceToLog(block: Block): Material? {
        val fenceType = block.type
        var logType: Material? = null
        when (fenceType) {
            Material.OAK_FENCE -> logType = Material.OAK_LOG
            Material.SPRUCE_FENCE -> logType = Material.SPRUCE_LOG
            Material.BIRCH_FENCE -> logType = Material.BIRCH_LOG
            Material.JUNGLE_FENCE -> logType = Material.JUNGLE_LOG
            Material.DARK_OAK_FENCE -> logType = Material.DARK_OAK_LOG
            Material.ACACIA_FENCE -> logType = Material.ACACIA_LOG
            else -> null
        }
        return logType
    }
    private fun getRandomTreeBlock(centerBlock: Block): Block? {
        val foundBlocks = findTreeInCube(centerBlock)
        if (foundBlocks.isEmpty()) { return null }
        val random = Random()
        val index = random.nextInt(foundBlocks.size)
        return foundBlocks[index]
    }
    fun getTreeMaterials(): List<Material> {
        val treeMaterials = mutableListOf<Material>()
        treeMaterials.add(Material.OAK_LOG)
        treeMaterials.add(Material.SPRUCE_LOG)
        treeMaterials.add(Material.BIRCH_LOG)
        treeMaterials.add(Material.JUNGLE_LOG)
        treeMaterials.add(Material.ACACIA_LOG)
        treeMaterials.add(Material.DARK_OAK_LOG)
        return treeMaterials
    }
    private fun getFenceMaterials(): List<Material> {
        val fenceMaterials = mutableListOf<Material>()
        fenceMaterials.add(Material.OAK_FENCE)
        fenceMaterials.add(Material.SPRUCE_FENCE)
        fenceMaterials.add(Material.BIRCH_FENCE)
        fenceMaterials.add(Material.JUNGLE_FENCE)
        fenceMaterials.add(Material.ACACIA_FENCE)
        fenceMaterials.add(Material.DARK_OAK_FENCE)
        return fenceMaterials
    }
    private fun getToolsList(): List<Material>{
        val toolsMaterials = mutableListOf<Material>()
        toolsMaterials.add(Material.GOLDEN_AXE)
        //Можно добавить больше различных предметов чем можно ломать
        return toolsMaterials
    }

    fun getTree(block: Block, blocks: MutableList<Block>) {
        val data = block.blockData as Byte
        val width = if ((data and 0x10.toByte()) != 0.toByte()) 2 else 1
        val height = if ((data.toInt() and 0x0F) == 1) 3 else 2

        for (i in 0 until width) {
            for (j in 0 until height) {
                for (k in 0 until width) {
                    val x = block.x + i - (width - 1) / 2
                    val y = block.y + j
                    val z = block.z + k - (width - 1) / 2
                    val currentBlock = block.world.getBlockAt(x, y, z)

                    if (currentBlock.type == block.type) {
                        blocks.add(currentBlock)
                    }
                }
            }
        }
    }
}