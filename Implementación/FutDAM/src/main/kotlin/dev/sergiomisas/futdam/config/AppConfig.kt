package dev.sergiomisas.futdam.config

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Properties

class AppConfig {

    lateinit var dbUrl: String
    lateinit var dataPath: String
    lateinit var jsonPath: String
    lateinit var htmlPath: String
    lateinit var csvPath: String
    lateinit var xmlPath: String

    init {
        loadPropeties()
        initStorage()
    }



    private fun loadPropeties() {
        val properties = Properties()
        properties.load(ClassLoader.getSystemResourceAsStream("properties/config.properties"))
        dbUrl = properties.getProperty("db.url", "jdbc:sqlite:futdam.db")
        dataPath = properties.getProperty("data.path", "data")
        jsonPath = dataPath + File.separator + properties.getProperty("json.path", "json")
        htmlPath = dataPath + File.separator + properties.getProperty("html.path", "html")
        csvPath = dataPath + File.separator + properties.getProperty("csv.path", "csv")
        xmlPath = dataPath + File.separator + properties.getProperty("xml.path", "xml")

    }

    private fun initStorage() {
        Files.createDirectories(Paths.get(jsonPath))
        Files.createDirectories(Paths.get(htmlPath))
        Files.createDirectories(Paths.get(csvPath))
        Files.createDirectories(Paths.get(xmlPath))
    }

}
