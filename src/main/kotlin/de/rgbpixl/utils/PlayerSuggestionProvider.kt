package de.rgbpixl.utils

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

object PlayerSuggestionProvider: SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>?,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions> {
        val source = context?.source

        val playerNames = source?.playerNames

        if (playerNames != null) {
            for (playerName in playerNames) {
                builder?.suggest(playerName)
            }
        }
        return builder?.buildFuture()!!
    }
}