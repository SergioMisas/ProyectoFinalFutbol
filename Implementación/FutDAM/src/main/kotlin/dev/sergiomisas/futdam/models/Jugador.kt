package dev.sergiomisas.futdam.models

data class Jugador(
    val id: Long = 0L,
    val nombre: String,
    val apellidos: String,
    val edad: Int,
    val nacionalidad: String,
    val dorsal: Int = 0,
    val apodo: String,
    val posicion: PosicionJugador,
    val idEquipo: Long = 0
) {
    enum class PosicionJugador {
        PORTERO, DEFENSA, CENTROCAMPISTA, DELANTERO
    }
}
