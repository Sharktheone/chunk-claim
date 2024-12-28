package de.rgbpixl.commands

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.ServerCommandSource
import java.sql.Connection

class ClaimCommands(private val db: Connection?) {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {

    }
}