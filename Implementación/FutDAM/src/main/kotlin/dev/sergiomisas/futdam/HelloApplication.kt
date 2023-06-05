package dev.sergiomisas.futdam

import dev.sergiomisas.futdam.di.koinModule
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.koin.core.context.startKoin

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        startKoin {
            printLogger()
            modules(koinModule)
        }
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "FutDAM"
        stage.scene = scene
        stage.isResizable = false
        stage.show()
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
