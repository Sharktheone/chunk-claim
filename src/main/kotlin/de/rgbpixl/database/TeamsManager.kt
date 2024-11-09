package de.rgbpixl.database

import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

object TeamsManager {
    fun teamCreate(ctx: CommandContext<ServerCommandSource>): Int {
        return 1
    }

    fun teamDelete(ctx: CommandContext<ServerCommandSource>): Int {
        return 1
    }

    fun teamChangeName(ctx: CommandContext<ServerCommandSource>): Int {
        return 1
    }

    fun teamChangeColor(ctx: CommandContext<ServerCommandSource>): Int {
        return 1
    }

    fun teamAddPlayer(ctx: CommandContext<ServerCommandSource>): Int {
        return 1
    }

    fun teamRemovePlayer(ctx: CommandContext<ServerCommandSource>): Int {
        return 1
    }

    fun teamInfo(ctx: CommandContext<ServerCommandSource>): Int {
        return 1
    }
}