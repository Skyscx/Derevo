package events

import App
import logic.Logic
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class TreeBreak(plugin: App) : Listener {
    private val logic = Logic(plugin)
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent){
        val tool = event.player.inventory.itemInMainHand.type
        val player = event.player
        if (logic.checkTypeToolsBreak(tool)){
            event.isCancelled = true
            val block = event.block
            val blockType = block.type
            val tree = logic.getTreeMaterials()
            if (blockType !in tree){ return }
            val listBlocks = logic.findTreeBlocks(block)
            logic.processingTreeBlocks(listBlocks, player)
            /*
            if (logic.checkTypeTreeBlock(blockType)){
                logic.replacerTreeToFence(blockType, block)
                logic.giveTreeToPlayer(player, blockType)
                logic.replacerFenceToTree(block, blockType, 20*10)

            }

            if(logic.checkTypeFenceBlock(blockType)) {
                val blockReplace = logic.giveFoundBlock(block)
                if (blockReplace != null) {
                    val blockReplaceType = blockReplace.type
                    if (logic.replacerTreeToFenceInRadius(blockReplace)) {
                        logic.giveTreeToPlayer(player, blockReplaceType)
                        logic.replacerFenceToTree(blockReplace, blockReplaceType, 20*10)
                    }
                }
                event.isCancelled = true

            }
            */

        }
    }
}