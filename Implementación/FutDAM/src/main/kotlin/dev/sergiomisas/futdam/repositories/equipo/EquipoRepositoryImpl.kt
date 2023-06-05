package dev.sergiomisas.futdam.repositories.equipo

import dev.sergiomisas.futdam.models.Equipo
import dev.sergiomisas.futdam.services.database.DatabaseManager
import java.sql.Statement


class EquipoRepositoryImpl(private val databaseManager: DatabaseManager) : EquipoRepository {
    val connection get() = databaseManager.connection

    override fun findByCountry(pais: String): List<Equipo> {
        val equipos = mutableListOf<Equipo>()
        connection.use {
            val sql = """
                SELECT * FROM equipo WHERE pais = ?
            """.trimIndent()

            val stmt = connection.prepareStatement(sql)
            stmt.use {
                it.setString(1, pais)

                val resultSet = it.executeQuery()
                resultSet.use { rs ->
                    while (rs.next()) {
                        val foundTeam = Equipo(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getString(5)
                        )
                        equipos.add(foundTeam)
                    }

                }
            }
        }
        return equipos
    }

    override fun save(item: Equipo): Equipo {
        val newId: Long
        connection.use { con ->
            val sql = """
                INSERT INTO equipo(nombre, pais, anyo_fundacion, liga) VALUES (?, ?, ?, ?)
            """.trimIndent()

            val stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            stmt.use {
                it.setString(1, item.nombre)
                it.setString(2, item.pais)
                it.setInt(3, item.anyoFundacion)
                it.setString(4, item.liga)

                it.executeUpdate()

                val newKey = it.generatedKeys

                newKey.use { key ->
                    newId = if (key.next()) key.getLong(1) else -1
                }
            }
        }
        return item.copy(id = newId)
    }

    override fun saveAll(items: List<Equipo>): List<Equipo> {
        items.forEach { equipo -> save(equipo) }
        return items
    }

    override fun findAll(): List<Equipo> {
        val equipos = mutableListOf<Equipo>()
        connection.use { con ->
            val sql = """
                SELECT * FROM equipo
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                val resultSet = it.executeQuery()
                resultSet.use { rs ->
                    while (rs.next()) {
                        val equipo = Equipo(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getInt(4),
                            rs.getString(5)
                        )
                        equipos.add(equipo)
                    }
                }
            }
        }
        return equipos
    }

    override fun findById(id: Long): Equipo? {
        val search: Equipo?
        connection.use { con ->
            val sql = """
                SELECT * FROM equipo WHERE id = ?
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                it.setLong(1, id)

                val resultSet = it.executeQuery()
                resultSet.use { rs ->
                    search = if (rs.next()) Equipo(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5)
                    ) else null
                }
            }
        }
        return search
    }

    override fun update(item: Equipo): Equipo? {
        val exists = findById(item.id)
        if (exists == null) return null
        else connection.use {con ->
            val sql = """
                UPDATE equipo SET nombre = ?, pais = ?, anyo_fundacion = ?, liga = ? WHERE id = ?
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                it.setString(1, item.nombre)
                it.setString(2, item.pais)
                it.setInt(3, item.anyoFundacion)
                it.setString(4, item.liga)
                it.setLong(5, item.id)

                it.executeUpdate()
            }
        }
        return exists
    }

    override fun delete(item: Equipo): Equipo? {
        val exists = findById(item.id)
        if (exists == null) return null
        else connection.use { con ->
            val sql = """
                DELETE FROM equipo WHERE id = ? 
            """.trimIndent()

            val stmt = con.prepareStatement(sql)
            stmt.use {
                it.setLong(1, item.id)
                it.executeUpdate()
            }
        }
        return exists
    }
}
