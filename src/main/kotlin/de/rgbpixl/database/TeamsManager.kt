package de.rgbpixl.database

import net.minecraft.util.Formatting
import java.sql.Connection
import java.util.UUID

object TeamsManager {
    fun teamCreate(name: String, owner: UUID, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun teamDelete(name: String, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun teamJoin(name: String, player: UUID, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun teamLeave(player: UUID, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun teamList(db: Connection?): List<String> {
        TODO("Not yet implemented")
    }

    fun teamInfo(name: String, db: Connection?): String {
        TODO("Not yet implemented")
    }

    fun teamSetOwner(name: String, owner: UUID, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun teamSetName(name: String, newName: String, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun teamSetColor(name: String, color: Formatting, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    fun teamSetPrefix(name: String, prefix: String, db: Connection?): Boolean {
        TODO("Not yet implemented")
    }
}