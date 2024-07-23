package de.rgbpixl.database

import java.util.UUID

object MoneyManager {
    fun getMoney(uuid: UUID): Int {
        Database.getConnection()?.use { conn ->
            val statement = conn.prepareStatement("SELECT * FROM players WHERE uuid = ?")
            statement.setString(1, uuid.toString())
            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                return resultSet.getInt("money")
            }
        }
        return 0
    }

    fun setMoney(uuid: UUID, money: Int) {
        Database.getConnection()?.use { conn ->
            val statement = conn.prepareStatement(
                """
                INSERT INTO players (uuid, money) VALUES (?, ?)
                ON DUPLICATE KEY UPDATE money = ?;
                """
            )
            statement.setString(1, uuid.toString())
            statement.setInt(2, money)
            statement.setInt(3, money)
            statement.executeUpdate()
        }
    }
}