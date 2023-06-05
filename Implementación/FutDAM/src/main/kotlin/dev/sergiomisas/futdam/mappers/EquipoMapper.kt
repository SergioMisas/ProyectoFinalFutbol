package dev.sergiomisas.futdam.mappers

import dev.sergiomisas.futdam.models.Equipo

fun Equipo.toDto() = dev.sergiomisas.futdam.dto.EquipoDto(
    id = id,
    nombre = nombre,
    pais = pais,
    anyoFundacion = anyoFundacion,
    liga = liga,
    jugadores = emptyList()
)
