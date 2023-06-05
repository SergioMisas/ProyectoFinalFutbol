package dev.sergiomisas.futdam.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiomisas.futdam.errors.JugadorError
import dev.sergiomisas.futdam.models.Jugador

fun Jugador.validate(): Result<Jugador, JugadorError> {
    if (nombre.isEmpty()) {
        return Err(JugadorError.JugadorInvalido("El nombre no puede estar vacío"))
    }
    if (apellidos.isEmpty()) {
        return Err(JugadorError.JugadorInvalido("Los apellidos no pueden estar vacíos"))
    }
    if (edad !in 16..50) {
        return Err(JugadorError.JugadorInvalido("La edad debe estar comprendida entre 16 y 50 años"))
    }
    if (nacionalidad.isEmpty()) {
        return Err(JugadorError.JugadorInvalido("La nacionalidad no puede estar vacía"))
    }
    when (idEquipo) {
        0L -> if (dorsal != 0)
            return Err(JugadorError.JugadorInvalido("El dorsal debe ser 0 si el jugador no tiene equipo"))

        else -> if (dorsal !in 1..99)
            return Err(JugadorError.JugadorInvalido("El dorsal debe estar comprendido entre 1 y 99"))
    }
    return Ok(this)
}
