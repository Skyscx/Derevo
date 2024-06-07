package events

import App
import logic.Logic
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.scheduler.BukkitRunnable

class TreeBreak(private val plugin: App) : Listener {
    private val logic = Logic(plugin)
    private val treeList = logic.getTreeMaterials()
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent){
        val tool = event.player.inventory.itemInMainHand.type
        val player = event.player
        val block = event.block
        val blockType = block.type
        if (logic.checkTypeToolsBreak(tool)){
            //val blockBreak = event.block
            //val blockBreakType = event.block.type
            if (blockType !in treeList){ return } //Проверка на тип блока

            // Поиск всех блоков дерева, связанных со сломанным блоком
            val treeBlocks = mutableListOf<Block>()
            logic.getTree(block, treeBlocks)

            for (currentBlock in treeBlocks){
                currentBlock.type = Material.AIR
            }
            println(blockType)
            val fenceType = logic.replacerTreeToFence(blockType)
            println(fenceType)
            if (fenceType == null) {
                player.sendMessage("Не удалось определить тип забора для дерева")

                return
            }

            val queue = ArrayDeque<Block>(treeBlocks)

            val timer = object : BukkitRunnable(){
                override fun run(){
                    //queue empty
                    if (queue.isEmpty()){
                        cancel()
                        return
                    }

                    //getBlockInQueue
                    val currentBlock = queue.removeFirst()
                    currentBlock.type = fenceType
                    logic.giveTreeToPlayer(player, currentBlock.type)

                    if (currentBlock.type in treeList){
                        val trunkBlocks = mutableListOf<Block>()
                        queue.addAll(trunkBlocks)
                    }
                }
            }

            timer.runTaskTimer(plugin, 1L, 1L)

            /**
             * for ((index, currentBlock) in treeBlocks.withIndex()) {
             *                 logic.replacerFenceToTree(currentBlock, fenceType, index * 2)
             *                 player.inventory.addItem(ItemStack(blockType, 1))
             *
             *             }
             *
             *
             * **/
            event.isCancelled

            /**

            if (logic.checkTypeTreeBlock(blockBreakType)){
                logic.replacerTreeToFence(blockBreakType, blockBreak)
                logic.giveTreeToPlayer(player, blockBreakType)
                logic.replacerFenceToTree(blockBreak, blockBreakType, 20*10) // 10 sec regeneration
                event.isCancelled = true
            }
            if (logic.checkTypeFenceBlock(blockBreakType)){
                val blockReplace = logic.giveFoundBlock(blockBreak)
                if (blockReplace != null){
                    val blockReplaceType = blockReplace.type
                    if (logic.replacerTreeToFenceInRadius(blockReplace)){
                        logic.giveTreeToPlayer(player, blockReplaceType)
                        logic.replacerFenceToTree(blockReplace, blockReplaceType, 20*10) // 10 sec regeneration
                    }
                }
                event.isCancelled = true
            }
            **/
        }
    }
}