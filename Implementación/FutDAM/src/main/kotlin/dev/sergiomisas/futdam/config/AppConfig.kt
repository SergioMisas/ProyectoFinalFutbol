package dev.sergiomisas.futdam.config

import mu.KotlinLogging
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class AppConfig {

    lateinit var dbUrl: String
    lateinit var dbPath: String
    lateinit var dataPath: String
    lateinit var jsonPath: String

    val logger = KotlinLogging.logger {}

    init {
        loadProperties()
        initStorage()
    }

    private fun loadProperties() {
        val properties = Properties()
        properties.load(ClassLoader.getSystemResourceAsStream("config.properties"))
        dbUrl = properties.getProperty("db.url", "jdbc:sqlite:futdam.db")
        dbPath = properties.getProperty("db.path", "futdam.db")
        dataPath = properties.getProperty("data.path", "data")
        jsonPath = dataPath + File.separator + properties.getProperty("json.path", "json")
    }

    private fun initStorage() {
        Files.createDirectories(Paths.get(jsonPath))
    }
}
