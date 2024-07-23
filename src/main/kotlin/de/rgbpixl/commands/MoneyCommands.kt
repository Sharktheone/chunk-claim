package de.rgbpixl.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object MoneyCommands {
    fun register(dispatcher: CommandDispatcher<ServerCommandSource>, cra: CommandRegistryAccess, ce: CommandManager.RegistrationEnvironment) {
        dispatcher.register(CommandManager.literal("money")
            .executes{ ctx -> moneyInfo(ctx) })
    }

    private fun moneyInfo(ctx: CommandContext<ServerCommandSource>): Int {
        val source = ctx.source

        source.sendMessage(Text.literal("Testaaal"))
        return Command.SINGLE_SUCCESS
    }
}