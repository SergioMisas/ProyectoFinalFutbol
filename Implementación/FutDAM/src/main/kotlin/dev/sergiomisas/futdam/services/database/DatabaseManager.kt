package dev.sergiomisas.futdam.services.database

import dev.sergiomisas.futdam.config.AppConfig
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.DriverManager

class DatabaseManager() : KoinComponent {

    private val appConfig: AppConfig by inject()
    val connection get() = DriverManager.getConnection(appConfig.dbUrl)

    init {
        createDatabase()
        initDatabase()
    }

    fun createDatabase() {
        connection.use { con ->
            val sqlEquipo = """
                CREATE TABLE IF NOT EXISTS equipo (
                    id INT PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    pais TEXT NOT NULL,
                    anyo_fundacion INT NOT NULL,
                    liga TEXT NOT NULL,
                );
            """.trimIndent()

            val stmtEquipo = con.prepareStatement(sqlEquipo)
            stmtEquipo.use { it.executeUpdate() }

            val sqlJugador = """
                CREATE TABLE IF NOT EXISTS jugador (
                    id INT PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    apellidos TEXT NOT NULL,
                    edad INT NOT NULL,
                    nacionalidad TEXT NOT NULL,
                    dorsal INT NOT NULL,
                    apodo TEXT NOT NULL,
                    posicion TEXT NOT NULL,
                    id_equipo FOREIGN KEY REFERENCES equipo(id)   
                );
            """.trimIndent()

            val stmtJugador = con.prepareStatement(sqlJugador)
            stmtJugador.use { it.executeUpdate() }

        }
    }

    fun initDatabase() {
        connection.use { con ->

            val sqlInsertAgentesLibres = """
                INSERT INTO equipo(id, nombre, pais, anyo_fundacion, liga) VALUES
                (0, 'Agentes Libres', '-', 0, '-')
            """.trimIndent()

            val stmtAgentesLibres = con.prepareStatement(sqlInsertAgentesLibres)
            stmtAgentesLibres.use { it.executeUpdate() }

            val sqlInsertEquipo = """
                INSERT INTO equipo(nombre, pais, anyo_fundacion, liga) VALUES
                 ('Real Madrid', 'España', 1902, 'Liga Santander'),
                 ('FC Barcelona', 'España', 1899, 'Liga Santander'),
                 ('Atletico Madrid', 'España', 1903, 'Liga Santander'),
                 ('Leganes SAD', 'España', 1928, 'Liga Smartbank'),
                 ('Bayern Munchen', 'Alemania', 1900, 'Bundesliga'),
                 ('Besiktas', 'Turquía', 1903, 'Super Lig'),
                 ('Al-Nassr', 'Arabia Saudí', 1955, 'Saudi Pro League')
        """.trimIndent()

            val stmtEquipo = con.prepareStatement(sqlInsertEquipo)
            stmtEquipo.use { it.executeUpdate() }

            val sqlInsertJugadores = """
                INSERT INTO jugador(nombre, apellidos, edad, nacionalidad, dorsal, apodo, posicion, id_equipo) VALUES 
                ('Thibaut', 'Courtois', 30, 'Bélgica', 1, 'Canguro', 'PORTERO', 1),
                ('Robert', 'Lewandowski', 34, 'Polonia', 9, '', 'DELANTERO', 2),
                ('Jan', 'Oblak', 28, 'Eslovenia', 13, '', 'PORTERO', 3),
                ('Jon', 'Karrikaburu', 21, 'España', 19, '', 'DELANTERO', 4),
                ('Leon', 'Goretzka', 29, 'Alemania', 8, '', 'CENTROCAMPISTA', 5),
                ('Vincent', 'Aboubakar', 30, 'Camerún', 10, '', 'DELANTERO', 6),
                ('Anderson', 'Talisca', 28, 'Brasil', 94, '', 'CENTROCAMPISTA', 7)
            """.trimIndent()

            val stmtJugadores = con.prepareStatement(sqlInsertJugadores)
            stmtJugadores.use { it.executeUpdate() }
        }

    }


}
