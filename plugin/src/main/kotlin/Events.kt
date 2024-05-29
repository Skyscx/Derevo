import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class Events : Listener {
    @EventHandler
    fun playerBreakBlock(event: BlockBreakEvent){
        val player = event.player
        val block = event.block
        app.logger.info("Player - " + player.name)
        app.logger.info("block - " + block)

        if (block.type in listOf(Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG)) {
            val fenceType = when (block.type) {
                Material.OAK_LOG -> Material.OAK_FENCE
                Material.BIRCH_LOG -> Material.BIRCH_FENCE
                Material.SPRUCE_LOG -> Material.SPRUCE_FENCE
                Material.ACACIA_LOG -> Material.ACACIA_FENCE
                Material.DARK_OAK_LOG -> Material.DARK_OAK_FENCE
                else -> null
            }
            if (fenceType != null) {
                replaceWoodWithFence(block, fenceType)
            }
        }
        event.isCancelled = true;
    }
    private fun replaceWoodWithFence(block: Block, fenceType: Material) {
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    val currentBlock = block.getRelative(x, y, z)
                    if (currentBlock.type in listOf(Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.ACACIA_LOG, Material.DARK_OAK_LOG) && currentBlock.getRelative(
                            BlockFace.DOWN).type != Material.AIR) {
                        currentBlock.type = fenceType
                    }
                }
            }
        }
    }

}