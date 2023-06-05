package dev.sergiomisas.futdam.errors

sealed class EquipoError(val message: String) {
    class EquipoInvalido(message: String) : EquipoError(message)
    class NombreRepetido(message: String) : EquipoError(message)
}
