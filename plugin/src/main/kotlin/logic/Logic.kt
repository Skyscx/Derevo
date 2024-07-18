package logic

import App
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class Logic(val plugin: App) {
    //Замена материала установленного блока с древесины на забор
    private fun replacerTreeToFence(tree: Material, block: Block){
        val fenceType = when (tree) {
            Material.OAK_LOG -> Material.OAK_FENCE
            Material.BIRCH_LOG -> Material.BIRCH_FENCE
            Material.SPRUCE_LOG -> Material.SPRUCE_FENCE
            Material.ACACIA_LOG -> Material.ACACIA_FENCE
            Material.DARK_OAK_LOG -> Material.DARK_OAK_FENCE
            Material.JUNGLE_LOG -> Material.JUNGLE_FENCE
            else -> null
        }
        if (fenceType != null) block.type = fenceType
    }
    //Замена материала установленного блока с забором на древесину с задержкой
    private fun replacerFenceToTree(block: Block, newMaterial: Material, ticks: Int) {
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            block.type = newMaterial
        }, ticks.toLong())
    }
    //Проверка, каким предметом был сломан блок.
    fun checkTypeToolsBreak(tools: Material) : Boolean{
        val toolsMaterial = getToolsList()
        return toolsMaterial.contains(tools)
    }
    //Выдача блока древесины игроку
    private fun giveTreeToPlayer(player: Player, tree: Material){
        player.inventory.addItem(ItemStack(tree))
    }
    //Поиск блоков древесины
    fun findTreeBlocks(centerBlock: Block): List<Block>{
        val listBlocks = mutableListOf<Block>()
        val listMaterial = getTreeMaterials()
        if (centerBlock.type !in listMaterial){
            println("Сломанный блок не содержит данных древесины!")
            return emptyList()
        }
        val queue = ArrayDeque<Block>()
        queue.add(centerBlock)
        while (queue.isNotEmpty()) {
            val currentBlock = queue.removeFirst()
            if (currentBlock !in listBlocks) {
                listBlocks.add(currentBlock)
                val neighbors = findTreeInCube(currentBlock)
                for (neighbor in neighbors) {
                    if (neighbor !in listBlocks) {
                        queue.add(neighbor)
                    }
                }
            }
        }
        return listBlocks
    }
    //Обработка списка с найденными блоками
    fun processingTreeBlocks(treeBlocks: List<Block>, player: Player){
        val firstBlock = treeBlocks[0]
        val firstBlockType = firstBlock.type
        giveTreeToPlayer(player, firstBlockType)
        replacerTreeToFence(firstBlockType, firstBlock)
        replacerFenceToTree(firstBlock, firstBlockType, 20*20)
        val newTreeBlocks = treeBlocks.drop(1)
        val sortedTreeBlocks = newTreeBlocks.sortedBy { block -> block.location.y }
        object : BukkitRunnable(){
            override fun run() {
                Thread.sleep(500L)
                for (block in sortedTreeBlocks) {
                    val blockType = block.type
                    giveTreeToPlayer(player, blockType)
                    Thread.sleep(500L)
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        replacerTreeToFence(blockType, block)
                    })
                    replacerFenceToTree(block, blockType, 20*20)
                }
            }
        }.runTaskAsynchronously(plugin)
    }
    //Нахождение однотипных блоков в области 3x3x3
    private fun findTreeInCube(centerBlock: Block): List<Block> {
        val foundBlocks = mutableListOf<Block>()
        val targetMaterials = getTreeMaterials()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val block = centerBlock.getRelative(x, y, z)
                    if (targetMaterials.contains(block.type) && block.type != Material.AIR) {
                        val typeLog = centerBlock.type
                        if (block.type == typeLog) {
                            foundBlocks.add(block)
                        }
                    }
                }
            }
        }
        return foundBlocks
    }
    //Список древесины
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
    //Список инструментов
    private fun getToolsList(): List<Material>{
        val toolsMaterials = mutableListOf<Material>()
        toolsMaterials.add(Material.GOLDEN_AXE)
        return toolsMaterials
    }

}