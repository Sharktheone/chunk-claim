package de.rgbpixl.commands

import com.mojang.brigadier.CommandDispatcher

import net.luckperms.api.LuckPermsProvider
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import java.sql.Connection

class TeamCommands(private val db: Connection?) {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal("CAMteam"))

    }

    private fun hasLPPermission(source: ServerCommandSource, permission: String): Boolean {
        val user = LuckPermsProvider.get().userManager.getUser(source.player!!.uuid) ?: return false
        return user.cachedData.permissionData.checkPermission(permission).asBoolean()
    }



}
