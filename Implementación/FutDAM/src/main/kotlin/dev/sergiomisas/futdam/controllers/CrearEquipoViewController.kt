package dev.sergiomisas.futdam.controllers

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.sergiomisas.futdam.models.Equipo
import dev.sergiomisas.futdam.validators.validate
import dev.sergiomisas.futdam.viewmodels.FutDamViewModel
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CrearEquipoViewController : KoinComponent {
    private val viewModel: FutDamViewModel by inject()

    @FXML
    private lateinit var textFieldNombre: TextField

    @FXML
    private lateinit var textFieldPais: TextField

    @FXML
    private lateinit var textFieldFundacion: TextField

    @FXML
    private lateinit var textFieldLiga: TextField

    @FXML
    private lateinit var buttonCrear: Button

    @FXML
    private lateinit var buttonCancelar: Button

    @FXML
    private fun initialize() {
        initEvents()
    }

    private fun initEvents() {
        buttonCrear.setOnAction { crearEquipo() }
        buttonCancelar.setOnAction { cerrarVentana() }
    }

    private fun crearEquipo() {
        val equipo = Equipo(
            nombre = textFieldNombre.text,
            pais = textFieldPais.text,
            anyoFundacion = textFieldFundacion.text.toIntOrNull() ?: 0,
            liga = textFieldLiga.text
        )
        equipo.validate().andThen { viewModel.crearEquipo(it) }
            .onSuccess { equipo ->
                viewModel.state.value = viewModel.state.value.copy(
                    equipos = viewModel.state.value.equipos + equipo
                )
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Crear Equipo"
                alert.headerText = "Equipo creado correctamente"
                alert.contentText = "Se ha creado el equipo ${equipo.nombre} correctamente"
                alert.showAndWait()
                cerrarVentana()
            }.onFailure { error ->
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Crear Equipo"
                alert.headerText = "Error al crear el equipo"
                alert.contentText = error.message
                alert.showAndWait()
            }
    }

    private fun cerrarVentana() {
        val stage = buttonCancelar.scene.window as Stage
        stage.close()
    }
}
