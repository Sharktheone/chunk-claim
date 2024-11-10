package de.rgbpixl.database

import java.sql.Connection

object MoneyManager {
    fun getMoney(name: String, db: Connection?): Int {
        val statement = db?.prepareStatement("SELECT money FROM players WHERE name = ?")
        statement?.setString(1, name)
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

    fun setMoney(name: String, money: Int, db: Connection?) {
        val statement = db?.prepareStatement(
            """
                UPDATE players SET money = ? WHERE name = ?;
            """
        )
        statement?.setInt(1, money)
        statement?.setString(2, name)
        statement?.executeUpdate()
        statement?.close()
    }
}