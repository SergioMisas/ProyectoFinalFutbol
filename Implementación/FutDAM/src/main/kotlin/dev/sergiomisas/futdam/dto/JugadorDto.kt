package dev.sergiomisas.futdam.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root


@Root(name = "jugador")
data class JugadorDto (
    @field:Attribute(name = "id")
    @param:Attribute(name = "id")
    val id: Long,

    @field:Element(name = "nombre")
    @param:Element(name = "nombre")
    val nombre: String,

    @field:Element(name = "apellidos")
    @param:Element(name = "apellidos")
    val apellidos: String,

    @field:Element(name = "edad")
    @param:Element(name = "edad")
    val edad: Int,

    @field:Element(name = "nacionalidad")
    @param:Element(name = "nacionalidad")
    val nacionalidad: String,

    @field:Element(name = "dorsal")
    @param:Element(name = "dorsal")
    val dorsal: Int,

    @field:Element(name = "apodo")
    @param:Element(name = "apodo")
    val apodo: String,

    @field:Element(name = "posicion")
    @param:Element(name = "posicion")
    val posicion: String,


)
