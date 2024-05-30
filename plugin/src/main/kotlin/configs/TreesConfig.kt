package configs

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class TreesConfig(private val file: File) {
    private var config: FileConfiguration

    init {
        this.config = YamlConfiguration.loadConfiguration(file)
    }
    fun reloadResourceConfig() {
        config = YamlConfiguration.loadConfiguration(file)
    }

    /**Getter and Setter */








}