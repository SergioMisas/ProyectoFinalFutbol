package dev.sergiomisas.futdam.mappers

import dev.sergiomisas.futdam.dto.EquipoDto
import dev.sergiomisas.futdam.models.Equipo


fun Equipo.toDto(): EquipoDto {
    return EquipoDto(
        this.id,
        this.nombre,
        this.pais,
        this.anyoFundacion,
        this.liga,
        listOf()
        )
}

fun EquipoDto.toEquipo(): Equipo {
    return Equipo(
        this.id,
        this.nombre,
        this.pais,
        this.anyoFundacion,
        this.liga
    )
}
