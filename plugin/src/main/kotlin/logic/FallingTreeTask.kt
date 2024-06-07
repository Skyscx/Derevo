package logic

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable

class FallingTreeTask(private val blocks: List<Block>, private val fenceType: Material) : BukkitRunnable() {
    private var index = 0

    override fun run() {
        if (index >= blocks.size) {
            cancel()
            return
        }

        val block = blocks[index]
        block.type = fenceType
        index++
    }
}