package dev.sergiomisas.futdam.repositories.equipo

import dev.sergiomisas.futdam.models.Equipo
import dev.sergiomisas.futdam.repositories.CrudRepository

interface EquipoRepository: CrudRepository<Equipo, Long> {
    fun findByCountry(pais: String): List<Equipo>
}
