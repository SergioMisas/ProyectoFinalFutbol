package dev.sergiomisas.futdam.viewmodels

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.sergiomisas.futdam.errors.EquipoError
import dev.sergiomisas.futdam.errors.JugadorError
import dev.sergiomisas.futdam.mappers.toDto
import dev.sergiomisas.futdam.models.Equipo
import dev.sergiomisas.futdam.models.Jugador
import dev.sergiomisas.futdam.repositories.equipo.EquipoRepository
import dev.sergiomisas.futdam.repositories.jugador.JugadorRepository
import dev.sergiomisas.futdam.services.storage.json.JsonStorageService
import javafx.beans.property.SimpleObjectProperty
import java.io.File

class FutDamViewModel(
    private val jugadorRepository: JugadorRepository,
    private val equipoRepository: EquipoRepository,
    private val jsonStorageService: JsonStorageService
) {
    val state = SimpleObjectProperty(FutDamState())

    init {
        initState()
    }

    data class FutDamState(
        val jugadores: List<Jugador> = emptyList(),
        val equipos: List<Equipo> = emptyList(),
        val jugadorSeleccionado: JugadorFormulario = JugadorFormulario(),
        val posiciones: List<String> = mutableListOf("") + Jugador.PosicionJugador.values().map { it.name }
    )

    data class JugadorFormulario(
        val nombre: String = "",
        val apellidos: String = "",
        val edad: String = "",
        val dorsal: String = "",
        val nacionalidad: String = "",
        val apodo: String = "",
        val posicion: String = "",
        val nombreEquipo: String = "",
        val paisEquipo: String = "",
        val fundacionEquipo: String = "",
        val ligaEquipo: String = ""
    )


    fun initState() {
        state.value = state.value.copy(
            jugadores = jugadorRepository.findAll(),
            equipos = equipoRepository.findAll()
        )
    }

    fun getJugadoresFiltrados(nombre: String, equipo: String, posicion: String): List<Jugador> {
        val listaJugadores = jugadorRepository.findAll()
        var equipoEditable = equipo
        return state.value.jugadores.filter {
            (it.nombre + it.apellidos).contains(nombre, ignoreCase = true) &&
                    equipoRepository.findById(it.idEquipo)!!.nombre.contains(equipo) &&
                    it.posicion.name.contains(posicion)
        }
    }

    fun getEquipoById(id: Long): String {
        return equipoRepository.findById(id)?.nombre ?: ""
    }

    fun getEquipoIdByJugador(jugador: JugadorFormulario): Long {
        return jugadorRepository.findAll().find {
            it.nombre == jugador.nombre &&
                    it.apellidos == jugador.apellidos &&
                    it.edad.toString() == jugador.edad &&
                    it.dorsal.toString() == jugador.dorsal &&
                    it.nacionalidad == jugador.nacionalidad &&
                    it.apodo == jugador.apodo &&
                    it.posicion.name == jugador.posicion
        }?.idEquipo ?: -1
    }

    fun setJugadorSeleccionado(jugador: Jugador) {
        val equipo = equipoRepository.findById(jugador.idEquipo)
        state.value = state.value.copy(
            jugadorSeleccionado = JugadorFormulario(
                jugador.nombre,
                jugador.apellidos,
                jugador.edad.toString(),
                jugador.dorsal.toString(),
                jugador.nacionalidad,
                jugador.apodo,
                jugador.posicion.name,
                equipo?.nombre ?: "",
                equipo?.pais ?: "",
                equipo?.anyoFundacion.toString(),
                equipo?.liga ?: ""
            )
        )
    }

    fun getJugadorByFormulario(formulario: JugadorFormulario): Jugador? {
        return jugadorRepository.findAll().find {
            it.nombre == state.value.jugadorSeleccionado.nombre &&
                    it.apellidos == state.value.jugadorSeleccionado.apellidos &&
                    it.edad.toString() == state.value.jugadorSeleccionado.edad &&
                    it.dorsal.toString() == state.value.jugadorSeleccionado.dorsal &&
                    it.nacionalidad == state.value.jugadorSeleccionado.nacionalidad &&
                    it.apodo == state.value.jugadorSeleccionado.apodo &&
                    it.posicion.name == state.value.jugadorSeleccionado.posicion
        }
    }


    fun borrarJugadorSeleccionado() {
        val jugador = getJugadorByFormulario(state.value.jugadorSeleccionado)
        // No puede fallar pero aún así lo pongo por precaución
        if (jugador != null) {
            jugadorRepository.delete(jugador)
        }
        state.value = state.value.copy(
            jugadores = jugadorRepository.findAll()
        )
    }

    fun borrarEquipoSeleccionado() {
        val jugador = getJugadorByFormulario(state.value.jugadorSeleccionado)
        val equipo = equipoRepository.findAll().find { it.id == jugador?.idEquipo }
        val jugadoresEquipo = jugadorRepository.findAllByTeam(equipo!!.id)
        jugadoresEquipo.forEach {
            jugadorRepository.update(it.copy(idEquipo = 0, dorsal = 0))
        }
        equipoRepository.delete(equipo)
        state.value = state.value.copy(
            jugadores = jugadorRepository.findAll(),
            equipos = equipoRepository.findAll()
        )
    }

    fun getNumJugadoresEquipo(id: Long): Int {
        return jugadorRepository.findAll().filter { it.idEquipo == id }.size
    }

    fun exportarEquipo(equipo: Equipo, file: File) {
        val jugadores = jugadorRepository.findAll().filter { it.idEquipo == equipo.id }
        val equipoDto = equipo.toDto()
        val jugadoresDto = jugadores.toDto()
        jsonStorageService.save(file, equipoDto.copy(jugadores = jugadoresDto))
    }

    fun crearJugador(jugador: Jugador): Result<Jugador, JugadorError> {
        val jugadores = jugadorRepository.findAll().filter { it.idEquipo == jugador.idEquipo }
        if (jugadores.size == 24) {
            return Err(JugadorError.EquipoCompleto("El equipo ya tiene 24 jugadores"))
        }
        if (jugador.dorsal in jugadores.map { it.dorsal }) {
            return Err(JugadorError.DorsalRepetido("El dorsal ya está en uso"))
        }
        return Ok(jugadorRepository.save(jugador))
    }

    fun editarJugador(jugadorNuevo: Jugador): Result<Jugador, JugadorError> {
        val jugadorActual = jugadorRepository.findById(jugadorNuevo.id)
        val jugadores = jugadorRepository.findAll().filter { it.idEquipo == jugadorNuevo.idEquipo }
        if (jugadores.size == 24) {
            return Err(JugadorError.EquipoCompleto("El equipo ya tiene 24 jugadores"))
        }
        if (jugadorNuevo.dorsal in jugadores.map { it.dorsal } && jugadorNuevo.idEquipo != 0L) {
            if (jugadorNuevo.idEquipo != jugadorActual?.idEquipo) {
                return Err(JugadorError.DorsalRepetido("El dorsal ya está en uso"))
            } else {
                if (jugadorNuevo.dorsal != jugadorActual.dorsal) {
                    return Err(JugadorError.DorsalRepetido("El dorsal ya está en uso"))
                }
            }
        }
        jugadorRepository.update(jugadorNuevo)
        return Ok(jugadorNuevo)
    }

    fun crearEquipo(equipo: Equipo): Result<Equipo, EquipoError> {
        val equipos = equipoRepository.findAll()
        if (equipo.nombre in equipos.map { it.nombre }) {
            return Err(EquipoError.NombreRepetido("El nombre ya está en uso"))
        }
        return Ok(equipoRepository.save(equipo))
    }

    fun editarEquipo(equipo: Equipo): Result<Equipo, EquipoError> {
        val equipoActual = equipoRepository.findById(equipo.id)!!
        val equipos = equipoRepository.findAll()
        if (equipo.nombre in equipos.map { it.nombre } && equipo.nombre != equipoActual.nombre) {
            return Err(EquipoError.NombreRepetido("El nombre ya está en uso"))
        }
        equipoRepository.update(equipo)
        return Ok(equipo)
    }
}
