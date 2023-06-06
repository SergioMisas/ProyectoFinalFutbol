package dev.sergiomisas.futdam.validators

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiomisas.futdam.errors.EquipoError
import dev.sergiomisas.futdam.models.Equipo
import java.time.LocalDate

fun Equipo.validate(): Result<Equipo, EquipoError> {
    if (nombre.isEmpty()) {
        return Err(EquipoError.EquipoInvalido("El nombre del equipo no puede estar vacío"))
    }
    if (pais.isEmpty()) {
        return Err(EquipoError.EquipoInvalido("El país del equipo no puede estar vacío"))
    }
    if (anyoFundacion !in 1798..LocalDate.now().year) {
        return Err(
            EquipoError.EquipoInvalido(
                "La fecha de fundación del equipo debe estar entre 1798 y ${LocalDate.now().year}"
            )
        )
    }
    if (liga.isEmpty()) {
        return Err(EquipoError.EquipoInvalido("La liga del equipo no puede estar vacía"))
    }
    return Ok(this)
}
