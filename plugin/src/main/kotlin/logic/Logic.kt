package logic

import App
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.scheduler.BukkitRunnable


class Logic(val plugin: App) {
    fun replacerTreeToFence(tree: Material, block: Block){
        val fenceType = when (tree) {
            Material.OAK_LOG -> Material.OAK_FENCE
            Material.BIRCH_LOG -> Material.BIRCH_FENCE
            Material.SPRUCE_LOG -> Material.SPRUCE_FENCE
            Material.ACACIA_LOG -> Material.ACACIA_FENCE
            Material.DARK_OAK_LOG -> Material.DARK_OAK_FENCE
            else -> null
        }
        if (fenceType != null) block.type = fenceType
    }
    fun replaceBlockWithTimer(block: Block, newMaterial: Material, ticks: Int) {
        object : BukkitRunnable() {
            override fun run() {
                block.type = newMaterial
            }
        }.runTaskLater(plugin, ticks.toLong())
    }
    fun checkTypeTreeBlock(tree: Material) : Boolean {
        val treeMaterials = getTreeMaterials()
        return treeMaterials.contains(tree)
    }
    fun checkTypeFenceBlock(fence: Material) : Boolean{
        val fenceMaterial = getFenceMaterials()
        return fenceMaterial.contains(fence)
    }
    fun checkTypeToolsBreak(tools: Material) : Boolean{
        val toolsMaterial = getToolsList()
        return toolsMaterial.contains(tools)
    }
    private fun getTreeMaterials(): List<Material> {
        val treeMaterials = mutableListOf<Material>()
        treeMaterials.add(Material.OAK_LOG)
        treeMaterials.add(Material.SPRUCE_LOG)
        treeMaterials.add(Material.BIRCH_LOG)
        treeMaterials.add(Material.JUNGLE_LOG)
        treeMaterials.add(Material.ACACIA_LOG)
        treeMaterials.add(Material.DARK_OAK_LOG)
        return treeMaterials
    }
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
    private fun getToolsList(): List<Material>{
        val toolsMaterials = mutableListOf<Material>()
        toolsMaterials.add(Material.GOLDEN_AXE)
        //Можно добавить больше различных предметов чем можно ломать
        return toolsMaterials
    }
}