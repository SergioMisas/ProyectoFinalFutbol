package dev.sergiomisas.futdam.mappers

import dev.sergiomisas.futdam.dto.JugadorDto
import dev.sergiomisas.futdam.models.Jugador

fun Jugador.toDto(): JugadorDto {
    return JugadorDto(
        id = id,
        nombre = nombre,
        apellidos = apellidos,
        edad = edad,
        nacionalidad = nacionalidad,
        dorsal = dorsal,
        apodo = apodo,
        posicion = posicion.name
    )
}

fun List<Jugador>.toDto(): List<JugadorDto> {
    return map { it.toDto() }
}
