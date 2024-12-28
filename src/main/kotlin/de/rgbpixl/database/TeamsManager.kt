package de.rgbpixl.database

import net.minecraft.util.Formatting
import java.sql.Connection
import java.util.UUID

object TeamsManager {
    fun teamCreate(name: String, color: Formatting?, owner: UUID, db: Connection?): Int {
        // Prepares the statement for creating a new team
        val createTeam = db?.prepareStatement("INSERT INTO teams (name, color, owner) VALUES (?, ?, ?) RETURNING id")
        createTeam?.setString(1, name)
        createTeam?.setString(2, color.toString())
        createTeam?.setString(3, owner.toString())


        // Check if team already exists
        val checkIfTeamExists = db?.prepareStatement("SELECT * FROM teams WHERE name = ?")
        checkIfTeamExists?.setString(1, name)
        val result = checkIfTeamExists?.executeQuery()
        if (result != null) {
            if (result.next()) {
                checkIfTeamExists.close()
                result.close()
                return -1
            }
        }

        val createTeamReturn = createTeam?.executeQuery()
        var teamId = -1
        if (createTeamReturn != null && createTeamReturn.next()) {
            teamId = createTeamReturn.getInt(1)
        }
        createTeam?.close()
        return teamId
    }

    fun teamDelete(team: Int, db: Connection?): Int {
        // Statement to delete the team
        val deleteTeam = db?.prepareStatement("DELETE FROM teams WHERE id = ?")
        deleteTeam?.setInt(1, team)

        deleteTeam?.executeUpdate()
        deleteTeam?.close()
        return 1
    }

    fun teamChangeName(newName: String, uuid: java.util.UUID, db: Connection?): Int {
        TODO("Not yet implemented")
    }

    fun teamChangeColor(newColor: String, db: Connection?): Int {
        TODO("Not yet implemented")
    }

    fun teamAddPlayer(team: Int, player: UUID, db: Connection?): Int {
        // Statement to add player to the team
        val addPlayerToTeam = db?.prepareStatement("UPDATE players SET team_id = ? WHERE uuid = ?")
        addPlayerToTeam?.setInt(1, team)
        addPlayerToTeam?.setString(2, player.toString())

        // Check if player is in a team
        val checkIfPlayerIsInTeam = db?.prepareStatement("SELECT * FROM players WHERE uuid = ?")
        checkIfPlayerIsInTeam?.setString(1, player.toString())
        val result = checkIfPlayerIsInTeam?.executeQuery()
        if (result != null) {
            if (result.next()) {
                val teamId = result.getInt("team_id")
                if (teamId != -1) {
                    checkIfPlayerIsInTeam.close()
                    result.close()
                    return -1
                }
            }
        }

        addPlayerToTeam?.executeUpdate()
        addPlayerToTeam?.close()
        return 1
    }

    fun teamRemovePlayer(name: String, db: Connection?): Int {
        TODO("Not yet implemented")
    }

    fun teamLeave(name: String, db: Connection?): Int {
        TODO("Not yet implemented")
    }

    fun teamInfo(teamId: Int, db: Connection?): Int {
        TODO("Not yet implemented")
    }

    fun checkIfPlayerIsOwner(uuid: java.util.UUID, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun checkIfPlayerIsInTeam(uuid: java.util.UUID, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }
}