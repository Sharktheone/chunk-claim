package de.rgbpixl.utils

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import de.rgbpixl.Chunksandmoney
import net.minecraft.server.command.ServerCommandSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.ResultSet
import java.sql.Statement
import java.util.concurrent.CompletableFuture


object PlayerSuggestionProvider: SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>?,
        builder: SuggestionsBuilder?
    ): CompletableFuture<Suggestions>? {
        val logger: Logger = LoggerFactory.getLogger("chunks_and_money")
        if (builder == null || context == null) {
            return null
        }
        var getPlayers: Statement? = null
        var players: ResultSet? = null

        return try {
            getPlayers = Chunksandmoney.db?.createStatement()
            players = getPlayers?.executeQuery("SELECT name FROM players")

            if (players != null) {
                while(players.next()) {
                    val name = players.getString("name")
                    if (name != null) {
                        builder.suggest(name)
                    } else {
                        logger.warn("Name is null")
                    }
                }
            }
            builder.buildFuture()
        } catch (e: Exception) {
            logger.error("Error while getting players from database: ${e.message}")
            return Suggestions.empty()
        } finally {
            try {
                players?.close()
                getPlayers?.close()
            } catch (e: Exception) {
                logger.error("Error while closing database connection: ${e.message}")
            }
        }
    }
}