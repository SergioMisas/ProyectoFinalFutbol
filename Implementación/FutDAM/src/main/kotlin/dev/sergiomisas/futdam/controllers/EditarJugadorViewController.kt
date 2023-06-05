package dev.sergiomisas.futdam.controllers

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.sergiomisas.futdam.models.Jugador
import dev.sergiomisas.futdam.validators.validate
import dev.sergiomisas.futdam.viewmodels.FutDamViewModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EditarJugadorViewController : KoinComponent {
    val viewModel: FutDamViewModel by inject()

    @FXML
    private lateinit var comboBoxEquipo: ComboBox<String>

    @FXML
    private lateinit var textFieldNombre: TextField

    @FXML
    private lateinit var textFieldApellido: TextField

    @FXML
    private lateinit var textFieldEdad: TextField

    @FXML
    private lateinit var textFieldNacionalidad: TextField

    @FXML
    private lateinit var textFieldApodo: TextField

    @FXML
    private lateinit var textFieldDorsal: TextField

    @FXML
    private lateinit var comboBoxPosicion: ComboBox<String>

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
        // ComboBox
        comboBoxEquipo.items = FXCollections.observableArrayList(viewModel.state.value.equipos.map { it.nombre })
        comboBoxEquipo.value = viewModel.state.value.equipos
            .find { it.id == viewModel.getEquipoIdByJugador(viewModel.state.value.jugadorSeleccionado) }!!.nombre

        textFieldNombre.text = viewModel.state.value.jugadorSeleccionado.nombre
        textFieldApellido.text = viewModel.state.value.jugadorSeleccionado.apellidos
        textFieldEdad.text = viewModel.state.value.jugadorSeleccionado.edad
        textFieldNacionalidad.text = viewModel.state.value.jugadorSeleccionado.nacionalidad
        textFieldApodo.text = viewModel.state.value.jugadorSeleccionado.apodo
        textFieldDorsal.text = viewModel.state.value.jugadorSeleccionado.dorsal

        comboBoxPosicion.items = FXCollections.observableArrayList(viewModel.state.value.posiciones.drop(1))
        comboBoxPosicion.value = viewModel.state.value.jugadorSeleccionado.posicion

        if (comboBoxEquipo.selectionModel.selectedItem == "Agentes Libres") {
            textFieldDorsal.isDisable = true
            textFieldDorsal.text = "0"
        } else {
            textFieldDorsal.isDisable = false
        }
    }

    private fun initEvents() {
        buttonEditar.setOnAction {
            editarJugador()
        }
        buttonCancelar.setOnAction {
            cerrarVentana()
        }
        comboBoxEquipo.setOnAction {
            if (comboBoxEquipo.selectionModel.selectedItem == "Agentes Libres") {
                textFieldDorsal.isDisable = true
                textFieldDorsal.text = "0"
            } else {
                textFieldDorsal.isDisable = false
            }
        }
    }

    private fun editarJugador() {
        val jugadorActual = viewModel.getJugadorByFormulario(viewModel.state.value.jugadorSeleccionado)!!
        val jugador = Jugador(
            id = jugadorActual.id,
            nombre = textFieldNombre.text,
            apellidos = textFieldApellido.text,
            edad = textFieldEdad.text.toIntOrNull() ?: -1,
            nacionalidad = textFieldNacionalidad.text,
            apodo = textFieldApodo.text,
            dorsal = textFieldDorsal.text.toIntOrNull() ?: -1,
            posicion = Jugador.PosicionJugador.valueOf(comboBoxPosicion.selectionModel.selectedItem),
            idEquipo = viewModel.state.value.equipos.find { it.nombre == comboBoxEquipo.value }!!.id
        )
        jugador.validate().andThen { viewModel.editarJugador(it) }
            .onSuccess { jugadorNuevo ->
                viewModel.state.value = viewModel.state.value.copy(
                    jugadores = viewModel.state.value.jugadores - jugadorActual + jugadorNuevo
                )
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Editar Jugador"
                alert.headerText = "Jugador editado correctamente"
                alert.contentText = "Se ha editado el jugador ${jugadorNuevo.nombre} ${jugadorNuevo.apellidos} correctamente"
                alert.showAndWait()
                cerrarVentana()
            }.onFailure { error ->
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Editar Jugador"
                alert.headerText = "Error al editar el jugador"
                alert.contentText = error.message
                alert.showAndWait()
            }
    }

    private fun cerrarVentana() {
        val stage = buttonCancelar.scene.window as Stage
        stage.close()
    }
}
