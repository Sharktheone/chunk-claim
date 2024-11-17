package de.rgbpixl.utils

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

object ColorSuggestionProvider: SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource?>?,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions?>? {
        return CompletableFuture.supplyAsync {
            builder?.suggest("black")
            builder?.suggest("dark_blue")
            builder?.suggest("dark_green")
            builder?.suggest("dark_aqua")
            builder?.suggest("dark_red")
            builder?.suggest("dark_purple")
            builder?.suggest("gold")
            builder?.suggest("gray")
            builder?.suggest("dark_gray")
            builder?.suggest("blue")
            builder?.suggest("green")
            builder?.suggest("aqua")
            builder?.suggest("red")
            builder?.suggest("light_purple")
            builder?.suggest("yellow")
            builder?.suggest("white")
            builder?.build()
        }
    }
}