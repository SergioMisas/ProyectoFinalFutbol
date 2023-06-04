package dev.sergiomisas.futdam.repositories.jugador

import dev.sergiomisas.futdam.models.Jugador
import dev.sergiomisas.futdam.services.database.DatabaseManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Statement

class JugadorRepositoryImpl : JugadorRepository, KoinComponent {
    val database: DatabaseManager by inject()
    val connection get() = database.connection

    override fun findAllByTeam(idEquipo: Long): List<Jugador> {
        val jugadoresFound = mutableListOf<Jugador>()
        connection.use { con ->
            val sql = """
                SELECT * FROM jugador WHERE id = ?
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                it.setLong(1, idEquipo)

                val resultSet = it.executeQuery()
                resultSet.use { rs ->
                    while (rs.next()) jugadoresFound.add(
                        Jugador(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getString(5),
                            rs.getInt(6),
                            rs.getString(7),
                            Jugador.PosicionJugador.valueOf(rs.getString(8)),
                            rs.getLong(9)
                        )
                    )
                }
            }
        }
        return jugadoresFound

    }

    override fun save(item: Jugador): Jugador {
        val newId: Long
        connection.use { con ->
            val sql = """
                INSERT INTO jugador(nombre, apellidos, edad, nacionalidad, dorsal, posicion, id_equipo) VALUES (?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

            val stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            stmt.use {
                it.setString(1, item.nombre)
                it.setString(2, item.apellidos)
                it.setInt(3, item.edad)
                it.setString(4, item.nacionalidad)
                it.setInt(5, item.dorsal)
                it.setString(6, item.posicion.toString())
                it.setLong(7, item.idEquipo)

                newId = it.generatedKeys.getLong(1)
                it.executeUpdate()
            }
        }
        return item.copy(id = newId)
    }

    override fun saveAll(items: List<Jugador>): List<Jugador> {
        items.forEach { item -> save(item) }
        return items
    }

    override fun findAll(): List<Jugador> {
        val jugadoresFound = mutableListOf<Jugador>()
        connection.use { con ->
            val sql = """
                SELECT * FROM jugador
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                val resultSet = it.executeQuery()
                resultSet.use { rs ->
                    while (rs.next()) jugadoresFound.add(
                        Jugador(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getString(5),
                            rs.getInt(6),
                            rs.getString(7),
                            Jugador.PosicionJugador.valueOf(rs.getString(8)),
                            rs.getLong(9)
                        )
                    )
                }
            }
        }
        return jugadoresFound
    }

    override fun findById(id: Long): Jugador? {
        val existe: Jugador?
        connection.use { con ->
            val sql = """
                SELECT * FROM jugador WHERE id = ?
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                it.setLong(1, id)

                val resultSet = it.executeQuery()
                resultSet.use { rs ->
                    existe = if (rs.next()) Jugador(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        Jugador.PosicionJugador.valueOf(rs.getString(8)),
                        rs.getLong(9)
                    ) else null
                }
            }
        }
        return existe
    }

    override fun update(item: Jugador): Jugador? {
        val existe = findById(item.id)
        if (existe == null) return null
        else {
            connection.use { con ->
                val sql = """
                UPDATE jugador SET nombre = ?, apellidos = ?, edad = ?, nacionalidad = ?, dorsal = ?, apodo = ?, posicion = ?, idEquipo = ? WHERE id = ?
            """.trimIndent()

                val stmt = con.prepareStatement(sql)
                stmt.use {
                    it.setString(1, item.nombre)
                    it.setString(2, item.apellidos)
                    it.setInt(3, item.edad)
                    it.setString(4, item.nacionalidad)
                    it.setInt(5, item.dorsal)
                    it.setString(6, item.apodo)
                    it.setString(7, item.posicion.toString())
                    it.setLong(8, item.idEquipo)
                    it.setLong(9, item.id)


                    it.executeUpdate()
                }
            }
        }
        return existe
    }

    override fun delete(item: Jugador): Jugador? {
        val existe = findById(item.id)
        if (existe == null) return null
        else {
            connection.use { con ->
                val sql= """
                    DELETE FROM jugador WHERE id = ?
                """.trimIndent()

                val stmt = con.prepareStatement(sql)
                stmt.use {
                    it.setLong(1, item.id)
                    it.executeUpdate()
                }
            }
        }
        return item
    }
}
