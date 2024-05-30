package events

import App
import logic.Logic
import org.bukkit.Material
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
            if (logic.checkTypeTreeBlock(blockBreakType)){
                logic.replacerTreeToFence(blockBreakType, blockBreak)
                val newMaterial = Material.OAK_LOG//TODO:НАПИСАТЬ ЛОГИКУ (Попробовать newMaterial -> blockBreakType)
                logic.replaceBlockWithTimer(blockBreak, newMaterial, 20*10) // 10 sec regeneration
                event.isCancelled = true
            }
            if (logic.checkTypeFenceBlock(blockBreakType)){
                //Поиск и замена рандомного
                //Если нету - то отмена
                event.isCancelled = true
            }

        }
    }
}