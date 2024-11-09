package de.rgbpixl.utils

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.LoaderOptions
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.nodes.Tag
import org.yaml.snakeyaml.representer.Representer
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter


data class Config (
    var database: Database = Database(),
    var chunks: Chunks = Chunks(),
    var misc: Misc = Misc()
) {
    data class Database (
        var host: String = "localhost",
        var port: Int = 3306,
        var database: String = "chunks_and_money",
        var user: String = "user",
        var password: String = "password"
    )

    data class Chunks(
        var maxChunks: Int = 32,
        var paymentMethod: String = "oneTime",
        var price: String = "x*10"
    )

    data class Misc(
        var currency: String = "pixls",
        var debug: Boolean = false,
        var ppb: Boolean = false
    )
}

object ConfigManager {
    private val configFile = File("config/chunksAndMoney/config.yml")
    private val yaml: Yaml

    init {
        val options = DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        options.indent = 2
        options.indicatorIndent = 2
        options.defaultScalarStyle = DumperOptions.ScalarStyle.PLAIN
        val loaderOptions = LoaderOptions()
        loaderOptions.setTagInspector { tag: Tag -> tag.className == Config::class.java.name }
        yaml = Yaml(Constructor(Config::class.java, loaderOptions), Representer(options))
    }

    var config: Config = Config()

    fun loadConfig() {
        if (configFile.exists()) {
            FileInputStream(configFile).use { inputStream ->
                config = yaml.load(inputStream)
            }
        } else {
            saveConfig()
        }
    }

    private fun saveConfig() {
        FileWriter(configFile).use { writer ->
            yaml.dump(config, writer)
        }
        loadConfig()
    }
}