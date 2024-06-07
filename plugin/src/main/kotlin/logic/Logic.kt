package logic

import App
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*


class Logic(private val plugin: App) {
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
    //Получение случайного найденного блока
    fun giveFoundBlock(centerBlock: Block): Block? {
        return getRandomTreeBlock(centerBlock)
    }
    /*fun replacerTreeToFenceInRadius(foundBlock: Block) : Boolean{
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
    }*/
    //Замена материала установленного блока с забором на древесину с задержкой
    private fun replacerFenceToTree(block: Block, newMaterial: Material, ticks: Int) {
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            block.type = newMaterial
        }, ticks.toLong())
    }
    //Проверка на содержание материала в списке с деревьями. Возращение Boolean
    fun checkTypeTreeBlock(tree: Material) : Boolean {
        val treeMaterials = getTreeMaterials()
        return treeMaterials.contains(tree)
    }
    //Проверка на содержание материала в списке с забором. Возращение Boolean
    fun checkTypeFenceBlock(fence: Material) : Boolean{
        val fenceMaterial = getFenceMaterials()
        return fenceMaterial.contains(fence)
    }
    //Проверка, каким предметом был сломан блок. Возращение Boolean
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
        // Создайте очередь для хранения блоков, которые должны быть обработаны
        val queue = ArrayDeque<Block>()
        queue.add(centerBlock)
        // Обработайте блоки в очереди
        while (queue.isNotEmpty()) {
            val currentBlock = queue.removeFirst()
            // Если блок не в списке, добавьте его и найдите его соседей
            if (currentBlock !in listBlocks) {
                listBlocks.add(currentBlock)
                val neighbors = findTreeInCube(currentBlock)
                // Добавьте соседей в очередь, если они не в списке
                for (neighbor in neighbors) {
                    if (neighbor !in listBlocks) {
                        queue.add(neighbor)
                    }
                }
            }
        }
        println("Size blocks tree: " + listBlocks.size)
        return listBlocks
    }
    //Обработка списка с найденными блоками
    fun processingTreeBlocks(treeBlocks: List<Block>, player: Player){
        val sortedTreeBlocks = treeBlocks.sortedBy { block -> block.location.y }

        object : BukkitRunnable(){
            override fun run() {
                for (block in sortedTreeBlocks) {
                    val blockType = block.type
                    replacerTreeToFence(blockType, block)
                    giveTreeToPlayer(player, blockType)
                    Thread.sleep(500L)
                }
            }
        }.runTaskAsynchronously(plugin)
    }
    //Приватный метод который находит все однотипные блоки в радиусе 3x3x3
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
    //Конвертация материала с забора на древесину
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
    //Получение случайного блока из области 3x3x3
    private fun getRandomTreeBlock(centerBlock: Block): Block? {
        val foundBlocks = findTreeInCube(centerBlock)
        if (foundBlocks.isEmpty()) { return null }
        val random = Random()
        val index = random.nextInt(foundBlocks.size)
        return foundBlocks[index]
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
    //Список заборов
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
    //Список инструментов
    private fun getToolsList(): List<Material>{
        val toolsMaterials = mutableListOf<Material>()
        toolsMaterials.add(Material.GOLDEN_AXE)
        //Можно добавить больше различных предметов чем можно ломать
        return toolsMaterials
    }

}