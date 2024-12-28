package de.rgbpixl.database

import java.sql.Connection
import java.util.UUID

object PlayerManager {
    // Check if player is already in database
    fun checkIfPlayerExists(uuid: String, db: Connection?): Boolean {
        // Check if player is already in database
        val checkPlayer = db?.prepareStatement("SELECT * FROM players WHERE uuid = ?;")
        checkPlayer?.setString(1, uuid.toString())
        val player = checkPlayer?.executeQuery()
        // If player is already in database, do nothing
        if (player?.next() == true) {
            checkPlayer.close()
            player.close()
            return true
        }
        return false
    }

    fun updateName(uuid: UUID, name: String, db: Connection?) {
        val updateName = db?.prepareStatement("UPDATE players SET name = ? WHERE uuid = ?;")
        updateName?.setString(1, name)
        updateName?.setString(2, uuid.toString())
        updateName?.executeUpdate()
        updateName?.close()
    }

    fun createNewPlayer(uuid: UUID, name: String, db: Connection?) {
        // Create new player in database
        val createNewPlayer = db?.prepareStatement("INSERT INTO players (uuid, name, money, team_id) VALUES (?, ?, ?, ?);")
        createNewPlayer?.setString(1, uuid.toString())
        createNewPlayer?.setString(2, name)
        createNewPlayer?.setInt(3, 0)
        createNewPlayer?.setInt(4, -1)
        createNewPlayer?.executeUpdate()
        createNewPlayer?.close()
    }
}