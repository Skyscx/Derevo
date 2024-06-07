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
        }
    }
}