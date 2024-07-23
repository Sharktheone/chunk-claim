package de.rgbpixl.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private const val URL = "jdbc:mariadb://192.168.178.35:3306/mydatabase"
    private const val USER = "user"
    private const val PASSWORD = "password"

    init {
        try {
            Class.forName("org.mariadb.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    fun setupDatabase() {
        getConnection()?.use { connection ->
            connection.createStatement().use { statement ->
                statement.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS players (
                        uuid VARCHAR(36) NOT NULL PRIMARY KEY,
                        money INTEGER NOT NULL
                    );
                    """
                )
            }
        }
    }
}