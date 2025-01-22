package de.rgbpixl.database

import net.minecraft.util.Formatting
import java.sql.Connection
import java.util.UUID

object TeamsManager {
    fun teamCreate(name: String, owner: UUID, db: Connection?): Boolean {
        val statement = db?.prepareStatement("INSERT INTO teams (name, owner) VALUES (?, ?)")
        statement?.setString(1, name)
        statement?.setString(2, owner.toString())
        statement?.execute()
        statement?.close()
        return true
    }

    fun teamDelete(name: String, db: Connection?): Boolean {
        val statement = db?.prepareStatement("DELETE FROM teams WHERE name = ?")
        statement?.setString(1, name)
        statement?.execute()
        statement?.close()

        return true
    }

    fun teamJoin(name: String, player: UUID, db: Connection?): Boolean {
        val getTeamId = db?.prepareStatement("SELECT id FROM teams WHERE name = ?")
        getTeamId?.setString(1, name)
        val teamIdResult = getTeamId?.executeQuery()
        var teamId = 0
        if (teamIdResult != null) {
            if (teamIdResult.next()) {
                teamId = teamIdResult.getInt("id")
            }
        }
        getTeamId?.close()
        teamIdResult?.close()

        val updatePlayer = db?.prepareStatement("UPDATE players SET team_id = ? WHERE uuid = ?")
        updatePlayer?.setInt(1, teamId)
        updatePlayer?.setString(2, player.toString())
        updatePlayer?.executeUpdate()
        updatePlayer?.close()

        return true
    }

    fun teamLeave(player: UUID, db: Connection?): Boolean {
        val updatePlayer = db?.prepareStatement("UPDATE players SET team_id = -1 WHERE uuid = ?")
        updatePlayer?.setString(1, player.toString())
        updatePlayer?.executeUpdate()
        updatePlayer?.close()

        return true
    }

    fun teamList(db: Connection?): List<String> {
        val statement = db?.prepareStatement("SELECT name FROM teams")
        val result = statement?.executeQuery()
        val teams = mutableListOf<String>()
        if (result != null) {
            while (result.next()) {
                teams.add(result.getString("name"))
            }
        }
        statement?.close()
        result?.close()

        return teams
    }

    fun teamInfo(name: String, db: Connection?): MutableMap<String, String> {
        val returnValues = mutableMapOf<String, String>()
        val statement = db?.prepareStatement("SELECT * FROM teams WHERE name = ?")
        statement?.setString(1, name)
        val result = statement?.executeQuery()
        if (result != null) {
            if (result.next()) {
                returnValues["id"] = result.getString("id")
                returnValues["name"] = result.getString("name")
                returnValues["owner"] = result.getString("owner")
                returnValues["color"] = result.getString("color")
                returnValues["prefix"] = result.getString("prefix")
            }
        }
        result?.close()
        statement?.close()

        return returnValues
    }

    fun teamSetOwner(name: String, owner: UUID, db: Connection?): Boolean {
        val statement = db?.prepareStatement("UPDATE teams SET owner = ? WHERE name = ?")
        statement?.setString(1, owner.toString())
        statement?.setString(2, name)
        statement?.executeUpdate()
        statement?.close()

        return true
    }

    fun teamSetName(name: String, newName: String, db: Connection?): Boolean {
        val statement = db?.prepareStatement("UPDATE teams SET name = ? WHERE name = ?")
        statement?.setString(1, newName)
        statement?.setString(2, name)
        statement?.executeUpdate()
        statement?.close()

        return true
    }

    fun teamSetColor(name: String, color: Formatting, db: Connection?): Boolean {
        val statement = db?.prepareStatement("UPDATE teams SET color = ? WHERE name = ?")
        statement?.setString(1, color.toString())
        statement?.setString(2, name)
        statement?.executeUpdate()
        statement?.close()

        return true
    }

    fun teamSetPrefix(name: String, prefix: String, db: Connection?): Boolean {
        val statement = db?.prepareStatement("UPDATE teams SET prefix = ? WHERE name = ?")
        statement?.setString(1, prefix)
        statement?.setString(2, name)
        statement?.executeUpdate()
        statement?.close()

        return true
    }
}