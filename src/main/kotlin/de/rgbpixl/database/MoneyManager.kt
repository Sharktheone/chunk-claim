package de.rgbpixl.database

import java.sql.Connection
import java.util.UUID

object MoneyManager {
    fun getMoney(uuid: UUID, db: Connection?): Int {
        val statement = db?.prepareStatement("SELECT money FROM players WHERE uuid = ?")
        statement?.setString(1, uuid.toString())
        val result = statement?.executeQuery()
        if (result != null) {
            if (result.next()) {
                val money = result.getInt("money")
                result.close()
                return money
            }
        } else {
            return 0
        }
        statement.close()
        result.close()

        return 0
    }

    fun setMoney(uuid: UUID, money: Int, db: Connection?) {
        val statement = db?.prepareStatement(
            """
                INSERT INTO players (uuid, money) VALUES (?, ?)
                ON DUPLICATE KEY UPDATE money = ?;
            """
        )
        statement?.setString(1, uuid.toString())
        statement?.setInt(2, money)
        statement?.setInt(3, money)
        statement?.executeUpdate()
        statement?.close()
    }
}