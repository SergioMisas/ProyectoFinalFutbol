package dev.sergiomisas.futdam

import dev.sergiomisas.futdam.di.koinModule
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.koin.core.context.startKoin

class FutdamApplication : Application() {
    override fun start(stage: Stage) {
        startKoin {
            printLogger()
            modules(koinModule)
        }
        val fxmlLoader = FXMLLoader(FutdamApplication::class.java.getResource("views/main-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 240.0)
        stage.title = "Hello!"
        stage.scene = scene
        stage.show()
    }
}

fun main() {

    Application.launch(FutdamApplication::class.java)
}
