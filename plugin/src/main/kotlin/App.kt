
import events.FireballEvent
import org.bukkit.plugin.java.JavaPlugin

lateinit var app: App

class App : JavaPlugin(){
    override fun onEnable() {
        app = this
        //server.pluginManager.registerEvents(TreeBreak(this),this)
        server.pluginManager.registerEvents(FireballEvent(), this)
    }
}