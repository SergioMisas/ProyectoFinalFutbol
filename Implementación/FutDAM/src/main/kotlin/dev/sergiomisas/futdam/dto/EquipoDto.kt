package dev.sergiomisas.futdam.dto


data class EquipoDto(

    val id: Long,

    val nombre: String,

    val pais: String,

    val anyoFundacion: Int,

    val liga: String,

    val jugadores: List<JugadorDto>
)
