package dev.sergiomisas.futdam.errors

sealed class JugadorError(val message: String) {
    class JugadorInvalido(message: String) : JugadorError(message)
    class EquipoCompleto(message: String) : JugadorError(message)
    class DorsalRepetido(message: String) : JugadorError(message)
}
