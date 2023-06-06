package dev.sergiomisas.futdam.controllers

import dev.sergiomisas.futdam.models.Jugador
import dev.sergiomisas.futdam.viewmodels.FutDamViewModel
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.util.Callback
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import dev.sergiomisas.futdam.routes.RoutesManager

class HelloController : KoinComponent {
    val viewModel: FutDamViewModel by inject()


    @FXML
    private lateinit var menuItemExportar: MenuItem

    @FXML
    private lateinit var menuItemAcercaDe: MenuItem

    @FXML
    private lateinit var textFieldNombre: TextField

    @FXML
    private lateinit var comboBoxEquipo: ComboBox<String>

    @FXML
    private lateinit var comboBoxPosicion: ComboBox<String>

    @FXML
    private lateinit var tableJugadores: TableView<Jugador>

    @FXML
    private lateinit var columnNombre: TableColumn<Jugador, String>

    @FXML
    private lateinit var columnApellidos: TableColumn<Jugador, String>

    @FXML
    private lateinit var columnEquipo: TableColumn<Jugador, String>

    @FXML
    private lateinit var textFieldJugadorNombre: TextField

    @FXML
    private lateinit var textFieldJugadorApellidos: TextField

    @FXML
    private lateinit var textFieldJugadorEdad: TextField

    @FXML
    private lateinit var textFieldJugadorDorsal: TextField

    @FXML
    private lateinit var textFieldJugadorNacionalidad: TextField

    @FXML
    private lateinit var textFieldJugadorApodo: TextField

    @FXML
    private lateinit var textFieldJugadorPosicion: TextField

    @FXML
    private lateinit var buttonJugadorCrear: Button

    @FXML
    private lateinit var buttonJugadorEditar: Button

    @FXML
    private lateinit var buttonJugadorBorrar: Button

    @FXML
    private lateinit var textFieldEquipoNombre: TextField

    @FXML
    private lateinit var textFieldEquipoPais: TextField

    @FXML
    private lateinit var textFieldEquipoFundacion: TextField

    @FXML
    private lateinit var textFieldEquipoLiga: TextField

    @FXML
    private lateinit var buttonEquipoCrear: Button

    @FXML
    private lateinit var buttonEquipoEditar: Button

    @FXML
    private lateinit var buttonEquipoBorrar: Button

    @FXML
    private fun initialize() {
        bloquearCampos()
        initBindings()
        initEvents()
    }

    private fun bloquearCampos() {
        val campos = listOf(
            textFieldJugadorNombre,
            textFieldJugadorApellidos,
            textFieldJugadorEdad,
            textFieldJugadorDorsal,
            textFieldJugadorNacionalidad,
            textFieldJugadorApodo,
            textFieldJugadorPosicion,
            textFieldEquipoNombre,
            textFieldEquipoPais,
            textFieldEquipoFundacion,
            textFieldEquipoLiga
        )

        campos.forEach { it.isEditable = false }
    }

    private fun initBindings() {
        // ComboBoxes reactivos
        comboBoxEquipo.items = FXCollections.observableArrayList(
            mutableListOf("") + viewModel.state.value.equipos.map { it.nombre }
        )
        comboBoxEquipo.selectionModel.selectFirst()
        comboBoxPosicion.items = FXCollections.observableArrayList(viewModel.state.value.posiciones)
        comboBoxPosicion.selectionModel.selectFirst()

        // Tabla reactiva
        tableJugadores.items = FXCollections.observableArrayList(viewModel.state.value.jugadores)
        columnNombre.cellValueFactory = PropertyValueFactory("nombre")
        columnApellidos.cellValueFactory = PropertyValueFactory("apellidos")
        columnEquipo.cellValueFactory = Callback { SimpleStringProperty(viewModel.getEquipoById(it.value.idEquipo)) }

        // Listener
        viewModel.state.addListener { _, oldState, newState ->
            actualizarTabla(oldState, newState)
            actualizarFormulario(oldState, newState)
            actualizarComboBoxEquipos(oldState, newState)
        }
    }

    private fun initEvents() {
        // Menu
        menuItemExportar.setOnAction { onExportar() }
        menuItemAcercaDe.setOnAction { mostrarAcercaDe() }

        // Filtrado
        textFieldNombre.setOnKeyReleased { filtrarJugadores() }
        comboBoxEquipo.setOnAction { filtrarJugadores() }
        comboBoxPosicion.setOnAction { filtrarJugadores() }

        // Tabla
        tableJugadores.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) cambiarJugadorSeleccionado(newValue)
        }

        // Botones
        buttonJugadorCrear.setOnAction { crearJugador() }
        buttonJugadorEditar.setOnAction { editarJugador() }
        buttonJugadorBorrar.setOnAction { borrarJugador() }
        buttonEquipoCrear.setOnAction { crearEquipo() }
        buttonEquipoEditar.setOnAction { editarEquipo() }
        buttonEquipoBorrar.setOnAction { borrarEquipo() }

    }

    private fun actualizarTabla(oldState: FutDamViewModel.FutDamState, newState: FutDamViewModel.FutDamState) {
        if (oldState.jugadores != newState.jugadores) {
            tableJugadores.selectionModel.clearSelection()
            tableJugadores.items = FXCollections.observableArrayList(newState.jugadores)
        }
    }

    private fun actualizarFormulario(oldState: FutDamViewModel.FutDamState, newState: FutDamViewModel.FutDamState) {
        if (oldState.jugadorSeleccionado != newState.jugadorSeleccionado) {
            val jugador = newState.jugadorSeleccionado
            val equipo = newState.equipos
                .find { it.id == viewModel.getEquipoIdByJugador(jugador) }
            textFieldJugadorNombre.text = jugador.nombre
            textFieldJugadorApellidos.text = jugador.apellidos
            textFieldJugadorEdad.text = jugador.edad
            textFieldJugadorDorsal.text = jugador.dorsal
            textFieldJugadorNacionalidad.text = jugador.nacionalidad
            textFieldJugadorApodo.text = jugador.apodo
            textFieldJugadorPosicion.text = jugador.posicion
            textFieldEquipoNombre.text = equipo?.nombre
            textFieldEquipoPais.text = equipo?.pais
            textFieldEquipoFundacion.text = equipo?.anyoFundacion.toString()
            textFieldEquipoLiga.text = equipo?.liga
        }
    }

    private fun actualizarComboBoxEquipos(
        oldState: FutDamViewModel.FutDamState,
        newState: FutDamViewModel.FutDamState
    ) {
        if (oldState.equipos != newState.equipos) {
            comboBoxEquipo.items = FXCollections.observableArrayList(
                mutableListOf("") + newState.equipos.map { it.nombre }
            )
        }
    }

    private fun onExportar() {
        RoutesManager.initExportarView()
    }

    private fun mostrarAcercaDe() {
        RoutesManager.initAcercaDeView()
    }

    private fun filtrarJugadores() {
        val nombre = textFieldNombre.text
        val equipo = comboBoxEquipo.value ?: ""
        val posicion = comboBoxPosicion.value ?: ""
        val jugadoresFiltrados = viewModel.getJugadoresFiltrados(nombre, equipo, posicion)
        tableJugadores.selectionModel.clearSelection()
        tableJugadores.items = FXCollections.observableArrayList(jugadoresFiltrados)
    }

    private fun cambiarJugadorSeleccionado(jugador: Jugador) {
        viewModel.setJugadorSeleccionado(jugador)
    }

    private fun crearJugador() {
        RoutesManager.initCrearJugadorView()
    }

    private fun editarJugador() {
        RoutesManager.initEditarJugadorView()
    }

    private fun borrarJugador() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Borrar jugador"
        alert.headerText = "¿Estás seguro de que quieres borrar el jugador?"
        alert.contentText = "Vas a borrar el jugador: " +
                viewModel.state.value.jugadorSeleccionado.nombre + " " +
                viewModel.state.value.jugadorSeleccionado.apellidos
        val res = alert.showAndWait()
        when (res.get()) {
            ButtonType.OK -> viewModel.borrarJugadorSeleccionado()
            else -> println()
        }
    }

    private fun crearEquipo() {
        RoutesManager.initCrearEquipoView()
    }

    private fun editarEquipo() {
        val equipo = viewModel.state.value.equipos
            .find { it.id == viewModel.getEquipoIdByJugador(viewModel.state.value.jugadorSeleccionado) }!!
        if (equipo.id == 0L) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error"
            alert.headerText = "No se puede editar el equipo"
            alert.contentText = "El equipo \"Agentes libres\" no se puede editar."
            alert.showAndWait()
            return
        }
        RoutesManager.initEditarEquipoView()
    }

    private fun borrarEquipo() {
        val equipo = viewModel.state.value.equipos
            .find { it.id == viewModel.getEquipoIdByJugador(viewModel.state.value.jugadorSeleccionado) }!!
        if (equipo.id == 0L) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Error"
            alert.headerText = "No se puede borrar el equipo"
            alert.contentText = "El equipo \"Agentes libres\" no se puede borrar."
            alert.showAndWait()
            return
        }
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Borrar equipo"
        alert.headerText = "¿Estás seguro de que quieres borrar el equipo?"
        alert.contentText = "Vas a borrar el equipo: " +
                equipo.nombre +
                " y todos sus jugadores se irán a agentes libres."
        val res = alert.showAndWait()
        when (res.get()) {
            ButtonType.OK -> viewModel.borrarEquipoSeleccionado()
            else -> println()
        }
    }
}
