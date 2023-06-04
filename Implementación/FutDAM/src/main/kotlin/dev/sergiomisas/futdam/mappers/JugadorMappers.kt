package dev.sergiomisas.futdam.mappers

import dev.sergiomisas.futdam.dto.JugadorDto
import dev.sergiomisas.futdam.models.Jugador

fun Jugador.toDto(): JugadorDto {
    return JugadorDto(
        this.id,
        this.nombre,
        this.apellidos,
        this.edad,
        this.nacionalidad,
        this.dorsal,
        this.apodo,
        this.posicion.name
    )
}

fun JugadorDto.toJugador(): Jugador {
    return Jugador(
        this.id,
        this.nombre,
        this.apellidos,
        this.edad,
        this.nacionalidad,
        this.dorsal,
        this.apodo,
        Jugador.PosicionJugador.valueOf(this.posicion)
    )
}
