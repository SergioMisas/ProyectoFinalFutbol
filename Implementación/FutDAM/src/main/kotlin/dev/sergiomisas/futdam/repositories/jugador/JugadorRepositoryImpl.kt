package dev.sergiomisas.futdam.repositories.jugador

import dev.sergiomisas.futdam.models.Jugador
import dev.sergiomisas.futdam.services.database.DatabaseManager
import java.sql.Statement

class JugadorRepositoryImpl(val database: DatabaseManager) : JugadorRepository {

    val connection get() = database.connection

    override fun findAllByTeam(idEquipo: Long): List<Jugador> {
        val jugadoresFound = mutableListOf<Jugador>()
        connection.use { con ->
            val sql = """
                SELECT * FROM jugador WHERE id_equipo = ?
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
                INSERT INTO jugador(nombre, apellidos, edad, nacionalidad, dorsal, apodo, posicion, id_equipo)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

            val stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            stmt.use {
                it.setString(1, item.nombre)
                it.setString(2, item.apellidos)
                it.setInt(3, item.edad)
                it.setString(4, item.nacionalidad)
                it.setInt(5, item.dorsal)
                it.setString(6, item.apodo)
                it.setString(7, item.posicion.toString())
                it.setLong(8, item.idEquipo)
                it.executeUpdate()

                newId = it.generatedKeys.getLong(1)
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
        val jugador: Jugador?
        connection.use { con ->
            val sql = """
                SELECT * FROM jugador WHERE id = ?
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                it.setLong(1, id)

                val resultSet = it.executeQuery()
                resultSet.use { rs ->
                    jugador = if (rs.next()) Jugador(
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
        return jugador
    }

    override fun update(item: Jugador): Jugador? {
        val jugador = findById(item.id)
        if (jugador == null) return null
        else {
            connection.use { con ->
                val sql = """
                UPDATE jugador SET nombre = ?, apellidos = ?, edad = ?, nacionalidad = ?, dorsal = ?, apodo = ?, posicion = ?, id_equipo = ? WHERE id = ?
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
        return jugador
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
