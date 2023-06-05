package dev.sergiomisas.futdam.routes

import dev.sergiomisas.futdam.HelloApplication
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage

object RoutesManager {
    lateinit var app: HelloApplication

    enum class Views(val path: String) {
        MAIN("/views/hello-view.fxml"),
        ACERCADE("/views/acerca-de-view.fxml"),
        CREARJUGADOR("/views/crear-jugador-view.fxml"),
        CREAREQUIPO("/views/crear-equipo-view.fxml"),
        EDITARJUGADOR("/views/editar-jugador-view.fxml"),
        EDITAREQUIPO("/views/editar-equipo-view.fxml"),
        EXPORTAR("/views/exportar-view.fxml")
    }

    fun initMainView(stage: Stage) {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource(Views.MAIN.path))
        val scene = Scene(fxmlLoader.load())
        stage.title = "FutDAM"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }

    fun initExportarView() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource(Views.EXPORTAR.path))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "Exportar equipo"
        stage.scene = scene
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }

    fun initAcercaDeView() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource(Views.ACERCADE.path))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "Acerca de"
        stage.scene = scene
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }

    fun initCrearJugadorView() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource(Views.CREARJUGADOR.path))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "Crear jugador"
        stage.scene = scene
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }

    fun initCrearEquipoView() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource(Views.CREAREQUIPO.path))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "Crear equipo"
        stage.scene = scene
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }

    fun initEditarJugadorView() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource(Views.EDITARJUGADOR.path))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "Editar jugador"
        stage.scene = scene
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }

    fun initEditarEquipoView() {
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource(Views.EDITAREQUIPO.path))
        val scene = Scene(fxmlLoader.load())
        val stage = Stage()
        stage.title = "Editar equipo"
        stage.scene = scene
        stage.initModality(Modality.WINDOW_MODAL)
        stage.isResizable = false
        stage.show()
    }
}
