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

class EditarEquipoViewController : KoinComponent {
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
    private lateinit var buttonEditar: Button

    @FXML
    private lateinit var buttonCancelar: Button

    @FXML
    private fun initialize() {
        initBindings()
        initEvents()
    }

    private fun initBindings() {
        textFieldNombre.text = viewModel.state.value.jugadorSeleccionado.nombreEquipo
        textFieldPais.text = viewModel.state.value.jugadorSeleccionado.paisEquipo
        textFieldFundacion.text = viewModel.state.value.jugadorSeleccionado.fundacionEquipo
        textFieldLiga.text = viewModel.state.value.jugadorSeleccionado.ligaEquipo
    }

    private fun initEvents() {
        buttonEditar.setOnAction {
            editarEquipo()
        }

        buttonCancelar.setOnAction {
            cerrarVentana()
        }
    }

    private fun editarEquipo() {
        val equipoActual = viewModel.state.value.equipos
            .find { it.id == viewModel.getEquipoIdByJugador(viewModel.state.value.jugadorSeleccionado) }!!
        val equipo = Equipo(
            id = equipoActual.id,
            nombre = textFieldNombre.text,
            pais = textFieldPais.text,
            anyoFundacion = textFieldFundacion.text.toIntOrNull() ?: 0,
            liga = textFieldLiga.text
        )
        equipo.validate().andThen { viewModel.editarEquipo(it) }
            .onSuccess { equipoNuevo ->
                viewModel.state.value = viewModel.state.value.copy(
                    equipos = viewModel.state.value.equipos - equipoActual + equipoNuevo
                )
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Editar Equipo"
                alert.headerText = "Equipo editado correctamente"
                alert.contentText = "Se ha editado el equipo ${equipoNuevo.nombre} correctamente"
                alert.showAndWait()
                cerrarVentana()
            }.onFailure { error ->
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Editar Equipo"
                alert.headerText = "Error al editar el equipo"
                alert.contentText = error.message
                alert.showAndWait()
            }
    }

    private fun cerrarVentana() {
        val stage = buttonCancelar.scene.window as Stage
        stage.close()
    }
}
