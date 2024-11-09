package de.rgbpixl.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class MariaDB(url: String, port: Int, database: String, private val user: String, private val password: String) {
    private val con = "jdbc:mariadb://$url:$port/$database"

    fun getDatabaseConnection(): Connection? {
        try {
            return DriverManager.getConnection(con, user, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    fun setupDatabase(db: Connection?) {
        val moneyTable = db?.createStatement()
        moneyTable?.execute(
            """
            CREATE TABLE IF NOT EXISTS players (
                uuid VARCHAR(36) PRIMARY KEY,
                money INT
            );
            """
        )
        moneyTable?.close()

        val teamsTable = db?.createStatement()
        teamsTable?.execute(
            """
            CREATE TABLE IF NOT EXISTS teams (
                name VARCHAR(255) PRIMARY KEY NOT NULL,
                color VARCHAR(7),
                owner VARCHAR(36),
                members JSON,
                FOREIGN KEY (owner) REFERENCES players(uuid)
            );
            """
        )
        teamsTable?.close()
    }

}