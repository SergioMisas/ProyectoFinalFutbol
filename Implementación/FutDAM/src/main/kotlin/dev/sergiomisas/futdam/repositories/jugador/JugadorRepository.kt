package dev.sergiomisas.futdam.repositories.jugador

import dev.sergiomisas.futdam.models.Jugador
import dev.sergiomisas.futdam.repositories.CrudRepository

interface JugadorRepository: CrudRepository<Jugador, Long> {
    fun findAllByTeam(idEquipo: Long): List<Jugador>
}
