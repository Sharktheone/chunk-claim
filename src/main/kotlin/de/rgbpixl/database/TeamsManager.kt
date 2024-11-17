package de.rgbpixl.database

import java.sql.Connection

object TeamsManager {
    fun teamCreate(name: String, color: String, db: Connection?): Int {
        val statement = db?.prepareStatement("INSERT INTO teams (name, color) VALUES (?, ?)")
        statement?.setString(1, name)
        statement?.setString(2, color)
        statement?.executeUpdate()
        statement?.close()

        val statement2 = db?.prepareStatement("SELECT id FROM teams WHERE name = ?")
        statement2?.setString(1, name)
        val result = statement2?.executeQuery()
        if (result != null) {
            if (result.next()) {
                val id = result.getInt("id")
                statement2.close()
                result.close()
                return id
            }
        } else {
            statement2?.close()
            result?.close()
            return -1
        }
        return 1
    }

    fun teamDelete(name: String, db: Connection?): Int {
        val statement = db?.prepareStatement("DELETE FROM teams WHERE name = ?")
        statement?.setString(1, name)
        statement?.executeUpdate()
        statement?.close()

        return 1
    }

    fun teamChangeName(newName: String, uuid: java.util.UUID, db: Connection?): Int {
        val statement = db?.prepareStatement("SELECT id FROM teams WHERE owner = ?")
        statement?.setString(1, uuid.toString())
        val result = statement?.executeQuery()
        if (result != null) {
            if (result.next()) {
                val id = result.getInt("id")
                val statement2 = db.prepareStatement("UPDATE teams SET name = ? WHERE id = ?")
                statement2?.setString(1, newName)
                statement2?.setInt(2, id)
                statement2?.executeUpdate()
                statement2?.close()
                statement.close()
                result.close()
                return 1
            }
        } else {
            statement?.close()
            result?.close()
            return 0
        }
        return 1
    }

    fun teamChangeColor(newColor: String, db: Connection?): Int {
        return 1
    }

    fun teamAddPlayer(name: String, team: Int, db: Connection?): Int {
        val statement = db?.prepareStatement("UPDATE players SET team = ? WHERE name = ?")
        statement?.setInt(1, team)
        statement?.setString(2, name)
        statement?.executeUpdate()
        return 1
    }

    fun teamRemovePlayer(name: String, db: Connection?): Int {
        return 1
    }

    fun teamLeave(name: String, db: Connection?): Int {
        return 1
    }

    fun teamInfo(teamId: Int, db: Connection?): Int {
        return 1
    }

    fun checkIfPlayerIsOwner(uuid: java.util.UUID, db: Connection?): Boolean {
        val statement = db?.prepareStatement("SELECT owner FROM teams WHERE owner = ?")
        statement?.setString(1, uuid.toString())
        val result = statement?.executeQuery()
        if (result != null) {
            if (result.next()) {
                val owner = result.getString("owner")
                statement.close()
                result.close()
                return owner != null
            }
        } else {
            statement?.close()
            result?.close()
            return false
        }
        statement.close()
        result.close()
        return false
    }

    fun checkIfPlayerIsInTeam(uuid: java.util.UUID, db: Connection?): Boolean {
        val statement = db?.prepareStatement("SELECT team FROM players WHERE uuid = ?")
        statement?.setString(1, uuid.toString())
        val result = statement?.executeQuery()
        if (result != null) {
            if (result.next()) {
                val team = result.getString("team")
                statement.close()
                result.close()
                return team != null
            }
        } else {
            statement?.close()
            result?.close()
            return false
        }
        statement.close()
        result.close()
        return false
    }
}