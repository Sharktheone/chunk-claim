package de.rgbpixl.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import de.rgbpixl.Chunksandmoney
import de.rgbpixl.database.MoneyManager
import de.rgbpixl.utils.PlayerSuggestionProvider
import net.luckperms.api.LuckPermsProvider
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import java.sql.Connection

class MoneyCommands(private val db: Connection?) {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal("money")
            // Get info about the amount of money a player has
            .then(CommandManager.literal("info")
                .requires { source -> hasLPPermission(source, "chunksandmoney.money.info")}
                .executes{ ctx -> moneyInfo(ctx) }
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .requires { source -> hasLPPermission(source, "chunksandmoney.money.infoplayer")}
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                        .executes{ ctx -> moneyInfoArgument(ctx) }))

            // Admin command, set the amount of money a player has
            .then(CommandManager.literal("set")
                .requires { source -> hasLPPermission(source, "chunksandmoney.money.set")}
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                    .then(CommandManager.argument("pixls", IntegerArgumentType.integer(0))

                        .executes{ ctx -> moneySet(ctx) })))

            // Transfer Money from one player to another
            .then(CommandManager.literal("transfer")
                .requires { source -> hasLPPermission(source, "chunksandmoney.money.transfer")}
                .then(CommandManager.argument("player", EntityArgumentType.player())
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

    // Get info about the amount of money a player has
    private fun moneyInfoArgument(ctx: CommandContext<ServerCommandSource>): Int {
        // Get arguments and source
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        // Null check
        if (player == null) {
            source.sendMessage(Text.literal("Cannot call the command without arguments"))
            return 0
        }

        // Get pixels and send message
        val pixls = MoneyManager.getMoney(player.uuid, db)
        val playerName = player.name.literalString
        source.player!!.sendMessage(Text.literal("Player $playerName has $pixls pixls!"))
        return 1
    }

    // Get info about the amount of money you have
    private fun moneyInfo(ctx: CommandContext<ServerCommandSource>): Int {
        // Get the source
        val source = ctx.source
        if (source.player != null) {
            // Get the amount of money
            val pixls = MoneyManager.getMoney(source.player!!.uuid, db)
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
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val pixls = IntegerArgumentType.getInteger(ctx, "pixls")

        // Execute stuff
        MoneyManager.setMoney(player!!.uuid, pixls, db)
        source.player!!.sendMessage(Text.literal("Set ${player.name.literalString} money to $pixls pixls"))
        player.sendMessage(Text.literal("Your money got set to $pixls pixls"))
        return 1
    }

    // Transfer Money from one player to another
    private fun moneyTransfer(ctx: CommandContext<ServerCommandSource>): Int {
        // Get all the arguments and the source player
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val pixls = IntegerArgumentType.getInteger(ctx, "pixls")

        // Check, if you have enough money
        if (MoneyManager.getMoney(source.player!!.uuid, db) < pixls) {
            source.player!!.sendMessage(Text.literal("Not enough pixls to transfer!"))
        } else {
            // Transfer logic
            val toSetSource = MoneyManager.getMoney(source.player!!.uuid, db)-pixls
            val toSetPlayer = MoneyManager.getMoney(player!!.uuid, db)+pixls
            MoneyManager.setMoney(source.player!!.uuid, toSetSource, db)
            MoneyManager.setMoney(player.uuid, toSetPlayer, db = db)
            source.player!!.sendMessage(Text.literal("Transferred $pixls pixls to ${player.name.literalString}!"))
            player.sendMessage(Text.literal("Received $pixls pixls from ${source.player!!.name.literalString}!"))
        }
        return 1
    }
}