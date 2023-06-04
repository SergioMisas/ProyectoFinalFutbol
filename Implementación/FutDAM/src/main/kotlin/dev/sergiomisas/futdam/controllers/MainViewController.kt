package dev.sergiomisas.futdam.controllers

import javafx.fxml.FXML
import javafx.scene.control.Label

class MainViewController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to JavaFX Application!"
    }
}
