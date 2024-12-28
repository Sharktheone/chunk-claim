package de.rgbpixl.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.rgbpixl.Chunksandmoney
import de.rgbpixl.database.MoneyManager
import de.rgbpixl.utils.PlayerSuggestionProvider
import net.luckperms.api.LuckPermsProvider
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import java.sql.Connection

class MoneyCommands(private val db: Connection?) {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal("money")
            // Get help
            .then(CommandManager.literal("help")
                .requires { source -> hasLPPermission(source, "chunksandmoney.money.help")}
                .executes{ ctx -> moneyHelp(ctx) })

            // Get info about the amount of money a player has
            .then(CommandManager.literal("info")
                .requires { source -> hasLPPermission(source, "chunksandmoney.money.info")}
                .executes{ ctx -> moneyInfo(ctx) }
                .then(CommandManager.argument("player", StringArgumentType.string())
                    .requires { source -> hasLPPermission(source, "chunksandmoney.money.infoplayer")}
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                        .executes{ ctx -> moneyInfoArgument(ctx) }))

            // Admin command, set the amount of money a player has
            .then(CommandManager.literal("set")
                .requires { source -> hasLPPermission(source, "chunksandmoney.money.set")}
                .then(CommandManager.argument("player", StringArgumentType.string())
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                    .then(CommandManager.argument("pixls", IntegerArgumentType.integer(0))
                        .executes{ ctx -> moneySet(ctx) })))

            // Transfer Money from one player to another
            .then(CommandManager.literal("transfer")
                .requires { source -> hasLPPermission(source, "chunksandmoney.money.transfer")}
                .then(CommandManager.argument("player", StringArgumentType.string())
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                    .then(CommandManager.argument("pixls", IntegerArgumentType.integer(0))
                        .executes{ ctx -> moneyTransfer(ctx) })))
        )
    }

    // For permissions
    private fun hasLPPermission(source: ServerCommandSource, permission: String): Boolean {
        val user = LuckPermsProvider.get().userManager.getUser(source.player!!.uuid) ?: return false
        return user.cachedData.permissionData.checkPermission(permission).asBoolean()
    }

    // Help command
    private fun moneyHelp(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        source.player!!.sendMessage(Text.literal("Money Commands:"))
        source.player!!.sendMessage(Text.literal("/money info - Get info about your current balance"))
        if (hasLPPermission(source, "chunksandmoney.money.infoplayer")) {
            source.player!!.sendMessage(Text.literal("/money info [player] - Get info about the amount of money a player has"))
        }
        if (hasLPPermission(source, "chunksandmoney.money.set")) {
            source.player!!.sendMessage(Text.literal("/money set [player] [pixls] - Admin command, set the amount of money a player has"))
        }
        source.player!!.sendMessage(Text.literal("/money transfer [player] [pixls] - Transfer Money to another player"))
        return 1
    }

    // Get info about the amount of money a player has
    private fun moneyInfoArgument(ctx: CommandContext<ServerCommandSource>): Int {
        // Get arguments and source
        val source = ctx.source
        val player = StringArgumentType.getString(ctx, "player")
        // Null check
        if (player == null) {
            source.sendMessage(Text.literal("Cannot call the command without arguments"))
            return 0
        }

        // Get pixels and send message
        val pixls = MoneyManager.getMoney(player, db)
        source.player!!.sendMessage(Text.literal("Player $player has $pixls pixls!"))
        return 1
    }

    // Get info about the amount of money you have
    private fun moneyInfo(ctx: CommandContext<ServerCommandSource>): Int {
        // Get the source
        val source = ctx.source
        if (source.player != null) {
            // Get the amount of money
            val pixls = MoneyManager.getMoney(source.player!!.name.string, db)
            source.player!!.sendMessage(Text.literal("You have $pixls pixls!"))
        } else {
            // Error handeling for console
            Chunksandmoney.logger.info("You cannot call this command without arguments!")
        }
        return 1
    }

    // Admin command, set the amount of money a player has
    private fun moneySet(ctx: CommandContext<ServerCommandSource>): Int {
        // Get arguemnts and source
        val source = ctx.source
        val player = StringArgumentType.getString(ctx, "player")
        val pixls = IntegerArgumentType.getInteger(ctx, "pixls")

        // Execute stuff
        MoneyManager.setMoney(player, pixls, db)
        source.player!!.sendMessage(Text.literal("Set $player money to $pixls pixls"))
        source.server.playerManager.getPlayer(player)?.sendMessage(Text.literal("Your money got set to $pixls!"))
        return 1
    }

    // Transfer Money from one player to another
    private fun moneyTransfer(ctx: CommandContext<ServerCommandSource>): Int {
        // Get all the arguments and the source player
        val source = ctx.source
        val player = StringArgumentType.getString(ctx, "player")
        val pixls = IntegerArgumentType.getInteger(ctx, "pixls")

        // Check, if you have enough money
        if (MoneyManager.getMoney(source.player!!.name.string, db) < pixls) {
            source.player!!.sendMessage(Text.literal("Not enough pixls to transfer!"))
        } else {
            // Transfer logic
            val toSetSource = MoneyManager.getMoney(source.player!!.name.string, db)-pixls
            val toSetPlayer = MoneyManager.getMoney(player, db)+pixls
            MoneyManager.setMoney(source.player!!.name.string, toSetSource, db)
            MoneyManager.setMoney(player, toSetPlayer, db = db)
            source.player!!.sendMessage(Text.literal("Transferred $pixls pixls to $player!"))
            source.server.playerManager.getPlayer(player)?.sendMessage(Text.literal("Received $pixls pixls from ${source.player!!.name.string}!"))
        }
        return 1
    }
}