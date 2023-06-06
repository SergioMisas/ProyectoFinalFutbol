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

class CrearJugadorViewController : KoinComponent {
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
    private lateinit var buttonCrear: Button

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
        comboBoxEquipo.selectionModel.selectFirst()
        comboBoxPosicion.items = FXCollections.observableArrayList(viewModel.state.value.posiciones.drop(1))
        comboBoxPosicion.selectionModel.selectFirst()

        // Dorsal
        textFieldDorsal.isDisable = true
        textFieldDorsal.text = "0"
    }

    private fun initEvents() {
        buttonCrear.setOnAction {
            crearJugador()
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

    private fun crearJugador() {
        val jugador = Jugador(
            nombre = textFieldNombre.text,
            apellidos = textFieldApellido.text,
            edad = textFieldEdad.text.toIntOrNull() ?: -1,
            nacionalidad = textFieldNacionalidad.text,
            apodo = textFieldApodo.text,
            dorsal = textFieldDorsal.text.toIntOrNull() ?: -1,
            posicion = Jugador.PosicionJugador.valueOf(comboBoxPosicion.selectionModel.selectedItem),
            idEquipo = viewModel.state.value.equipos.find { it.nombre == comboBoxEquipo.value }!!.id
        )
        jugador.validate().andThen { viewModel.crearJugador(it) }
            .onSuccess { jugador ->
                viewModel.state.value = viewModel.state.value.copy(
                    jugadores = viewModel.state.value.jugadores + jugador
                )
                val alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Crear Jugador"
                alert.headerText = "Jugador creado correctamente"
                alert.contentText = "Se ha creado el jugador ${jugador.nombre} ${jugador.apellidos} correctamente"
                alert.showAndWait()
                cerrarVentana()
            }.onFailure { error ->
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Crear Jugador"
                alert.headerText = "Error al crear el jugador"
                alert.contentText = error.message
                alert.showAndWait()
            }
    }

    private fun cerrarVentana() {
        val stage = buttonCancelar.scene.window as Stage
        stage.close()
    }
}
