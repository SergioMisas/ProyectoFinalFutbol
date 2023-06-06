package dev.sergiomisas.futdam

import dev.sergiomisas.futdam.di.koinModule
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.koin.core.context.startKoin
import dev.sergiomisas.futdam.routes.RoutesManager

class HelloApplication : Application() {
    override fun start(stage: Stage) {
        startKoin {
            printLogger()
            modules(koinModule)
        }
        RoutesManager.apply {
            app = this@HelloApplication
        }.run {
            initMainView(stage)
        }
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}
