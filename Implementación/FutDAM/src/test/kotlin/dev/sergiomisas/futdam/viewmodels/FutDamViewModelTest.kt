package dev.sergiomisas.futdam.viewmodels

import dev.sergiomisas.futdam.models.Equipo
import dev.sergiomisas.futdam.models.Jugador
import dev.sergiomisas.futdam.repositories.equipo.EquipoRepository
import dev.sergiomisas.futdam.repositories.jugador.JugadorRepository
import dev.sergiomisas.futdam.services.storage.json.JsonStorageService
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

class FutDamViewModelTest {
    private val jugadorRepositoryMock: JugadorRepository = mock()
    private val equipoRepositoryMock: EquipoRepository = mock()
    private val jsonStorageServiceMock: JsonStorageService = mock()
    private val viewModel = FutDamViewModel(
        jugadorRepository = jugadorRepositoryMock,
        equipoRepository = equipoRepositoryMock,
        jsonStorageService = jsonStorageServiceMock
    )

    val equipoData = listOf(
        Equipo(
            id = 1,
            nombre = "Real Madrid",
            pais = "España",
            anyoFundacion = 1902,
            liga = "LaLiga"
        )
    )

    val jugadorData = listOf(
        Jugador(
            id = 1,
            nombre = "Sergio",
            apellidos = "Ramos",
            edad = 34,
            dorsal = 4,
            nacionalidad = "España",
            apodo = "SR4",
            posicion = Jugador.PosicionJugador.DEFENSA,
            idEquipo = 1
        )
    )

    val jugadorFormulario = FutDamViewModel.JugadorFormulario(
        nombre = "Sergio",
        apellidos = "Ramos",
        edad = "34",
        dorsal = "4",
        nacionalidad = "España",
        apodo = "SR4",
        posicion = "DEFENSA",
        nombreEquipo = "Real Madrid",
        paisEquipo = "España",
        fundacionEquipo = "1902",
        ligaEquipo = "LaLiga"
    )

    @Test
    fun getEquipoById() {
        `when`(equipoRepositoryMock.findById(equipoData[0].id)).thenReturn(equipoData[0])

        assertEquals(equipoData[0].nombre, viewModel.getEquipoById(equipoData[0].id))
    }

    @Test
    fun getEquipoIdByJugador() {
        `when`(jugadorRepositoryMock.findAll()).thenReturn(jugadorData)

        assertEquals(jugadorData[0].idEquipo, viewModel.getEquipoIdByJugador(jugadorFormulario))
    }

    @Test
    fun setJugadorSeleccionado() {
        `when`(equipoRepositoryMock.findById(jugadorData[0].id)).thenReturn(equipoData[0])

        viewModel.setJugadorSeleccionado(jugadorData[0])

        val jugadorSeleccionado = jugadorFormulario

        assertAll(
            { assertEquals(jugadorSeleccionado.nombre, viewModel.state.value.jugadorSeleccionado.nombre) },
            { assertEquals(jugadorSeleccionado.apellidos, viewModel.state.value.jugadorSeleccionado.apellidos) },
            { assertEquals(jugadorSeleccionado.edad, viewModel.state.value.jugadorSeleccionado.edad) },
            { assertEquals(jugadorSeleccionado.dorsal, viewModel.state.value.jugadorSeleccionado.dorsal) },
            { assertEquals(jugadorSeleccionado.nacionalidad, viewModel.state.value.jugadorSeleccionado.nacionalidad) },
            { assertEquals(jugadorSeleccionado.apodo, viewModel.state.value.jugadorSeleccionado.apodo) },
            { assertEquals(jugadorSeleccionado.posicion, viewModel.state.value.jugadorSeleccionado.posicion) },
            { assertEquals(jugadorSeleccionado.nombreEquipo, viewModel.state.value.jugadorSeleccionado.nombreEquipo) },
            { assertEquals(jugadorSeleccionado.paisEquipo, viewModel.state.value.jugadorSeleccionado.paisEquipo) },
            {
                assertEquals(
                    jugadorSeleccionado.fundacionEquipo,
                    viewModel.state.value.jugadorSeleccionado.fundacionEquipo
                )
            },
            { assertEquals(jugadorSeleccionado.ligaEquipo, viewModel.state.value.jugadorSeleccionado.ligaEquipo) }
        )
    }

    @Test
    fun getJugadorByFormulario() {
        `when`(jugadorRepositoryMock.findAll()).thenReturn(jugadorData)

        viewModel.setJugadorSeleccionado(jugadorData[0])

        assertEquals(jugadorData[0], viewModel.getJugadorByFormulario(jugadorFormulario))
    }

    @Test
    fun borrarJugadorSeleccionado() {
        `when`(jugadorRepositoryMock.findAllByTeam(any())).thenReturn(jugadorData)
        `when`(equipoRepositoryMock.findAll()).thenReturn(equipoData)

        viewModel.setJugadorSeleccionado(jugadorData[0])

        viewModel.borrarJugadorSeleccionado()

        assertEquals(emptyList<Equipo>(), viewModel.state.value.equipos)
    }

    @Test
    fun getNumJugadoresEquipo() {
        `when`(jugadorRepositoryMock.findAll()).thenReturn(jugadorData)

        assertEquals(1, viewModel.getNumJugadoresEquipo(equipoData[0].id))
    }
}
