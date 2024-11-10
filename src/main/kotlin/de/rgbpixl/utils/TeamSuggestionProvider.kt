package de.rgbpixl.utils

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import de.rgbpixl.Chunksandmoney
import net.minecraft.server.command.ServerCommandSource
import java.sql.ResultSet
import java.sql.Statement
import java.util.concurrent.CompletableFuture

object TeamSuggestionProvider: SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>?,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions> {
        if (context == null || builder == null) {
            return Suggestions.empty()
        }
        var getTeams: Statement? = null
        var teams: ResultSet? = null

        try {
            getTeams = Chunksandmoney.db?.createStatement()
            teams = getTeams?.executeQuery("SELECT name FROM teams")

            if (teams != null) {
                while(teams.next()) {
                    val name = teams.getString("name")
                    if (name != null) {
                        builder.suggest(name)
                    }
                }
            }

        } catch (e: Exception) {
            Chunksandmoney.logger.error("Error while getting team suggestions: ${e.message}")
            return Suggestions.empty()
        } finally {
            teams?.close()
            getTeams?.close()
        }

        return builder.buildFuture()!!
    }
}