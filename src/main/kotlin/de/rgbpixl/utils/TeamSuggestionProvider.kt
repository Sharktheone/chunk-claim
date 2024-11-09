package de.rgbpixl.utils

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

object TeamSuggestionProvider: SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>?,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions> {
        val source = context?.source

        val teamNames = source?.teamNames

        if (teamNames != null) {
            for (teamName in teamNames) {
                builder?.suggest(teamName)
            }
        }
        return builder?.buildFuture()!!
    }
}