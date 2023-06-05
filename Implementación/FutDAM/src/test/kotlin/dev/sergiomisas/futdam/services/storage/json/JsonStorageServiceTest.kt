package dev.sergiomisas.futdam.services.storage.json

import dev.sergiomisas.futdam.dto.EquipoDto
import dev.sergiomisas.futdam.dto.JugadorDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.nio.file.Files
import kotlin.io.path.exists
import kotlin.io.path.readText

class JsonStorageServiceTest {
    private val storage = JsonStorageService()

    @Test
    fun save() {
        val data = EquipoDto(
            id = 1,
            nombre = "Equipo Inventado FC",
            pais = "Suecilandia",
            anyoFundacion = 1900,
            liga = "Liga de las Leyendas",
            jugadores = listOf(
                JugadorDto(
                    id = 1,
                    nombre = "Jugador Inventado",
                    apellidos = "Apellidos Inventados",
                    edad = 20,
                    nacionalidad = "Suecilandia",
                    dorsal = 1,
                    apodo = "El Inventado",
                    posicion = "PORTERO",
                )
            )
        )

        val json = """
            {
              "id": 1,
              "nombre": "Equipo Inventado FC",
              "pais": "Suecilandia",
              "anyoFundacion": 1900,
              "liga": "Liga de las Leyendas",
              "jugadores": [
                {
                  "id": 1,
                  "nombre": "Jugador Inventado",
                  "apellidos": "Apellidos Inventados",
                  "edad": 20,
                  "nacionalidad": "Suecilandia",
                  "dorsal": 1,
                  "apodo": "El Inventado",
                  "posicion": "PORTERO"
                }
              ]
            }
        """.trimIndent()

        val file = Files.createTempFile("test", ".json")

        storage.save(file.toFile(), data)

        assertAll(
            { assertTrue(file.exists()) },
            { assertEquals(json, file.readText()) }
        )
    }
}
