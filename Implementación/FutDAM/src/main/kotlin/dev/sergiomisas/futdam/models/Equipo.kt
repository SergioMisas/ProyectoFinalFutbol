package dev.sergiomisas.futdam.models

data class Equipo(
    val id: Long,
    val nombre: String,
    val pais: String,
    val anyoFundacion: Int,
    val liga: String
)
