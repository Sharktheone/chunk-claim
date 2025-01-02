package de.rgbpixl.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Postgresql(url: String, port: Int, database: String, private val user: String, private val password: String) {
    private val con = "jdbc:postgresql://$url:$port/$database"

    fun getDatabaseConnection(): Connection? {
        try {
            return DriverManager.getConnection(con, user, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    fun setupDatabase(db: Connection?) {
        val teamsTable = db?.createStatement()
        teamsTable?.execute(
            """
            CREATE TABLE IF NOT EXISTS teams (
                id SERIAL PRIMARY KEY ,
                name VARCHAR(255) NOT NULL,
                color VARCHAR(7),
                prefix VARCHAR(255),
                owner VARCHAR(36)
            );
            """
        )
        teamsTable?.close()

        val playersTable = db?.createStatement()
        playersTable?.execute(
            """
            CREATE TABLE IF NOT EXISTS players (
                uuid VARCHAR(36) NOT NULL PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                money INT,
                team_id INT,
                FOREIGN KEY (team_id) REFERENCES teams(id)
            );
            """
        )
        playersTable?.close()

        val noTeam = db?.createStatement()
        noTeam?.execute(
            """
            INSERT INTO teams (id, name, color, owner) VALUES (-1, 'No Team', '#FFFFFF', '00000000-0000-0000-0000-000000000000');
            """
        )
        noTeam?.close()
    }

}