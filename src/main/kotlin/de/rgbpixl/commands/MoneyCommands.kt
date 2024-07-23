package de.rgbpixl.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import de.rgbpixl.Chunks_and_money
import de.rgbpixl.database.MoneyManager
import de.rgbpixl.utils.PlayerSuggestionProvider
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object MoneyCommands {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>, cra: CommandRegistryAccess, ce: CommandManager.RegistrationEnvironment) {
        dispatcher.register(CommandManager.literal("money")
            // Get info about the amount of money a player has
            .then(CommandManager.literal("info")
                .executes{ ctx -> moneyInfo(ctx) }
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                        .executes{ ctx -> moneyInfoArgument(ctx) }))

            // Admin command, set the amount of money a player has
            .then(CommandManager.literal("set")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                    .then(CommandManager.argument("pixls", IntegerArgumentType.integer(0))
                        .executes{ ctx -> moneySet(ctx) })))

            // Transfer Money from one player to another
            .then(CommandManager.literal("transfer")
                .then(CommandManager.argument("player", EntityArgumentType.player())
                    .suggests{ ctx, builder -> PlayerSuggestionProvider.getSuggestions(ctx, builder)}
                    .then(CommandManager.argument("pixls", IntegerArgumentType.integer(0))
                        .executes{ ctx -> moneyTransfer(ctx) })))
        )
    }

    // Get info about the amount of money a player has
    private fun moneyInfoArgument(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")

        val pixls = MoneyManager.getMoney(uuid = player.uuid)
        val playerName = player.name.literalString
        source.player!!.sendMessage(Text.literal("Player $playerName has $pixls pixls!"))
        return 1
    }

    private fun moneyInfo(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        if (source.player != null) {
            val pixls = MoneyManager.getMoney(uuid = source.player!!.uuid)
            source.player!!.sendMessage(Text.literal("You have $pixls pixls!"))
        } else {
            Chunks_and_money.logger.info("You cannot call this command without arguments!")
        }
        return 1
    }

    // Admin command, set the amount of money a player has
    private fun moneySet(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val pixls = IntegerArgumentType.getInteger(ctx, "pixls")

        MoneyManager.setMoney(player!!.uuid, pixls)
        source.player!!.sendMessage(Text.literal("Set ${player.name.literalString} money to $pixls pixls"))
        player.sendMessage(Text.literal("Your money got set to $pixls pixls"))
        return 1
    }

    // Transfer Money from one player to another
    private fun moneyTransfer(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source
        val player = EntityArgumentType.getPlayer(ctx, "player")
        val pixls = IntegerArgumentType.getInteger(ctx, "pixls")

        if (MoneyManager.getMoney(source.player!!.uuid) < pixls) {
            source.player!!.sendMessage(Text.literal("Not enough pixls to transfer!"))
        } else {
            val toSetSource = MoneyManager.getMoney(source.player!!.uuid)-pixls
            val toSetPlayer = MoneyManager.getMoney(player!!.uuid)+pixls
            MoneyManager.setMoney(source.player!!.uuid, toSetSource)
            MoneyManager.setMoney(player.uuid, toSetPlayer)
            source.player!!.sendMessage(Text.literal("Transfered $pixls pixls to ${player.name.literalString}!"))
            player.sendMessage(Text.literal("Recieved $pixls pixls from ${source.player!!.name.literalString}!"))
        }

        return 1
    }
}