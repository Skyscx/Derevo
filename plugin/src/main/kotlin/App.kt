
import events.TreeBreak
import org.bukkit.plugin.java.JavaPlugin


lateinit var app: App

class App : JavaPlugin(){
    override fun onEnable() {
        app = this
        server.pluginManager.registerEvents(TreeBreak(this),this)
        //todo: Переделать логику, игрок ломает блок дерева, а затем все оставшиеся блоки этого дерева заменяются потихоньку на забор, как будто само рубиться.
    }
}