package dev.sergiomisas.futdam.controllers

import dev.sergiomisas.futdam.config.AppConfig
import dev.sergiomisas.futdam.repositories.jugador.JugadorRepository
import dev.sergiomisas.futdam.services.database.DatabaseManager
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HelloController : KoinComponent {
    val repo: JugadorRepository by inject()

    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
        repo
    }
}
