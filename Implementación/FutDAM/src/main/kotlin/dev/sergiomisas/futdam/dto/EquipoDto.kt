package dev.sergiomisas.futdam.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "equipo")
data class EquipoDto(
    @field:Attribute(name = "id")
    @param:Attribute(name = "id")
    val id: Long,

    @field:Element(name = "nombre")
    @param:Element(name = "nombre")
    val nombre: String,

    @field:Element(name = "pais")
    @param:Element(name = "pais")
    val pais: String,

    @field:Element(name = "anyoFundacion")
    @param:Element(name = "anyoFundacion")
    val anyoFundacion: Int,

    @field:Element(name = "liga")
    @param:Element(name = "liga")
    val liga: String,

    @field:ElementList(name = "jugadores", inline = true)
    @param:ElementList(name = "jugadores", inline = true)
    val jugadores: List<JugadorDto>
)
