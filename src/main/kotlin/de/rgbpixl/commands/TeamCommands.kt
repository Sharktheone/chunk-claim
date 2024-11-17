package de.rgbpixl.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.rgbpixl.database.TeamsManager
import de.rgbpixl.utils.PlayerSuggestionProvider
import de.rgbpixl.utils.TeamSuggestionProvider
import net.luckperms.api.LuckPermsProvider
import net.minecraft.command.argument.ColorArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import java.sql.Connection

class TeamCommands(private val db: Connection?) {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal("team")
            // Create Team
            .then(CommandManager.literal("create")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.create") }
                .then(CommandManager.argument("name", StringArgumentType.string())
                    .then(CommandManager.argument("color", ColorArgumentType.color())
                        .executes { ctx -> teamCreate(ctx) })))

            // Delete Team
            .then(CommandManager.literal("delete")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.delete") }
                .then(CommandManager.argument("team", StringArgumentType.string())
                    .suggests { ctx, builder -> TeamSuggestionProvider.getSuggestions(ctx, builder) }
                    .executes { ctx -> teamDelete(ctx) }))

            // Change Team Name
            .then(CommandManager.literal("changename")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.changename") }
                .then(CommandManager.argument("new name", StringArgumentType.string())
                    .executes { ctx -> teamChangeName(ctx) }))

            // Change Team Color
            .then(CommandManager.literal("changecolor")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.changecolor") }
                .then(CommandManager.argument("color", ColorArgumentType.color())
                    .executes { ctx -> teamChangeColor(ctx) }))

            // Add Player to Team
            .then(CommandManager.literal("addplayer")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.addplayer") }
                .then(CommandManager.argument("player", StringArgumentType.string())
                    .suggests { ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder) }
                    .executes { ctx -> teamAddPlayer(ctx) }))

            // Remove Player from Team
            .then(CommandManager.literal("removeplayer")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.removeplayer") }
                .then(CommandManager.argument("player", StringArgumentType.string())
                    .suggests { ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder) }
                    .executes { ctx -> teamRemovePlayer(ctx) }))

            // Leave Team
            .then(CommandManager.literal("leave")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.leave") }
                .executes { ctx -> teamLeave(ctx) })

            // Get Team Info
            .then(CommandManager.literal("info")
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.info") }
                .executes { ctx -> teamInfo(ctx) }
            .then(CommandManager.argument("team", StringArgumentType.string())
                .requires { source -> hasLPPermission(source, "chunksandmoney.team.infoargument") }
                .suggests { ctx, builder -> TeamSuggestionProvider.getSuggestions(ctx, builder) }
                .executes { ctx -> teamInfoArgument(ctx) }))
        )
    }

    private fun hasLPPermission(source: ServerCommandSource, permission: String): Boolean {
        val user = LuckPermsProvider.get().userManager.getUser(source.player!!.uuid) ?: return false
        return user.cachedData.permissionData.checkPermission(permission).asBoolean()
    }


    private fun teamCreate(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamChangeName(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamChangeColor(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamDelete(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamInfo(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamInfoArgument(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamAddPlayer(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamRemovePlayer(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }

    private fun teamLeave(ctx: CommandContext<ServerCommandSource>): Int {
        TODO("Not yet implemented")
    }
}
