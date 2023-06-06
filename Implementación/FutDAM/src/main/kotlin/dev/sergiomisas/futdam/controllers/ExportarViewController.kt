package dev.sergiomisas.futdam.controllers

import dev.sergiomisas.futdam.config.AppConfig
import dev.sergiomisas.futdam.viewmodels.FutDamViewModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class ExportarViewController : KoinComponent {
    val viewModel: FutDamViewModel by inject()
    val appConfig: AppConfig by inject()

    @FXML
    private lateinit var comboBoxEquipo: ComboBox<String>

    @FXML
    private lateinit var textFieldJugadores: TextField

    @FXML
    private lateinit var textFieldPais: TextField

    @FXML
    private lateinit var textFieldFundacion: TextField

    @FXML
    private lateinit var textFieldLiga: TextField

    @FXML
    private lateinit var buttonExportar: Button

    @FXML
    private lateinit var buttonCancelar: Button

    @FXML
    private fun initialize() {
        bloquearCampos()
        initBindings()
        initEvents()
    }

    private fun bloquearCampos() {
        textFieldJugadores.isEditable = false
        textFieldPais.isEditable = false
        textFieldFundacion.isEditable = false
        textFieldLiga.isEditable = false
    }

    fun initBindings() {
        // ComboBox
        comboBoxEquipo.items = FXCollections.observableArrayList(viewModel.state.value.equipos.map { it.nombre })

        // Listener
        comboBoxEquipo.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            actualizarCampos(newValue)
        }
    }


    fun initEvents() {
        buttonExportar.setOnAction {
            exportarEquipo()
        }

        buttonCancelar.setOnAction {
            cerrarVentana()
        }
    }

    private fun cerrarVentana(){
        val stage = buttonCancelar.scene.window as Stage
        stage.close()
    }

    private fun exportarEquipo() {
        val equipo = viewModel.state.value.equipos.find { it.nombre == comboBoxEquipo.selectionModel.selectedItem }
        if (equipo == null) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error al exportar"
            alert.headerText = "Error al exportar equipo"
            alert.contentText = "Seleccione un equipo para exportar"
            alert.showAndWait()
            return
        }
        val fileChooser = FileChooser()
        fileChooser.title = "Exportar equipo"
        fileChooser.initialDirectory = File(appConfig.jsonPath)
        fileChooser.initialFileName = "${equipo.nombre}.json"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("JSON", "*.json")
        )
        val file = fileChooser.showSaveDialog(buttonCancelar.scene.window as Stage)
        viewModel.exportarEquipo(equipo, file)
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "Exportar equipo"
        alert.headerText = "Equipo exportado correctamente"
        alert.contentText = "Se ha exportado el equipo ${equipo.nombre} correctamente"
        alert.showAndWait()
        cerrarVentana()
    }

    private fun actualizarCampos(newValue: String) {
        val equipo = viewModel.state.value.equipos.find { it.nombre == newValue }
        textFieldJugadores.text = viewModel.getNumJugadoresEquipo(equipo!!.id).toString()
        textFieldPais.text = equipo.pais
        textFieldFundacion.text = equipo.anyoFundacion.toString()
        textFieldLiga.text = equipo.liga
    }

}
