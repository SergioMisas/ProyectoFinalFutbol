package dev.sergiomisas.futdam.di

import dev.sergiomisas.futdam.config.AppConfig
import dev.sergiomisas.futdam.repositories.jugador.JugadorRepository
import dev.sergiomisas.futdam.repositories.jugador.JugadorRepositoryImpl
import dev.sergiomisas.futdam.services.database.DatabaseManager
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.single


val koinModule = module {
    single<AppConfig>(named("appconfig")) { AppConfig() }
    single<DatabaseManager>(named("databasemanager")) { DatabaseManager() }
    single<JugadorRepository>(named("jugadorrepository")) {JugadorRepositoryImpl()}
}
