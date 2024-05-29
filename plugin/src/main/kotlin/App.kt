import org.bukkit.plugin.java.JavaPlugin

lateinit var app: App

class App : JavaPlugin() {
    private lateinit var events: Events
    override fun onEnable() {
        app = this
        events = Events()
        server.pluginManager.registerEvents(events, this)
    }
}