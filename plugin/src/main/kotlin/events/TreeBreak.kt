package events

import App
import logic.Logic
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class TreeBreak : Listener {
    private val logic = Logic()
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent){
        //Проверка на предмет, которым ломают.
        val tool = event.player.inventory.itemInMainHand.type
        if (logic.checkTypeToolsBreak(tool)){
            val blockBreak = event.block
            val blockBreakType = event.block.type
            if (logic.checkTypeTreeBlock(blockBreakType)){
                logic.replacerTreeToFence(blockBreakType)
                val newMaterial = Material.OAK_FENCE//Написать логину
                logic.replaceBlockWithTimer(blockBreak, newMaterial, 20*10) // 10 sec regeneration
                event.isCancelled = true
            }
            if (logic.checkTypeFenceBlock(blockBreakType)){
                //Поиск и замена рандомного
            }

        }
    }
}