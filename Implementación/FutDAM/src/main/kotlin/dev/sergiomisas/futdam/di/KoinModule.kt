package dev.sergiomisas.futdam.di

import dev.sergiomisas.futdam.config.AppConfig
import dev.sergiomisas.futdam.repositories.equipo.EquipoRepository
import dev.sergiomisas.futdam.repositories.equipo.EquipoRepositoryImpl
import dev.sergiomisas.futdam.repositories.jugador.JugadorRepository
import dev.sergiomisas.futdam.repositories.jugador.JugadorRepositoryImpl
import dev.sergiomisas.futdam.services.database.DatabaseManager
import org.koin.dsl.module

val koinModule = module {
    single { AppConfig() }
    single { DatabaseManager(get()) }
    single<JugadorRepository> { JugadorRepositoryImpl(get()) }
    single<EquipoRepository> { EquipoRepositoryImpl(get()) }
}
