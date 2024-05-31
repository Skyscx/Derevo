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
        if (logic.checkTypeToolsBreak(tool)){
            val blockBreak = event.block
            val blockBreakType = event.block.type
            val player = event.player
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
                        logic.giveTreeToPlayer(player, blockReplaceType) //PROBLEM
                        logic.replacerFenceToTree(blockReplace, blockReplaceType, 20*10) // 10 sec regeneration //PROBLEM
                    }
                }
                event.isCancelled = true
            }

        }
    }
}