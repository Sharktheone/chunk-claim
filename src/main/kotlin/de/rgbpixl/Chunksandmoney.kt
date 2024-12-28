package de.rgbpixl

import de.rgbpixl.commands.ClaimCommands
import de.rgbpixl.commands.MoneyCommands
import de.rgbpixl.commands.TeamCommands
import de.rgbpixl.database.PlayerManager
import de.rgbpixl.database.Postgresql
import de.rgbpixl.utils.ConfigManager
import de.rgbpixl.utils.PapiProxyBridgeManager
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.network.ServerPlayNetworkHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection

object Chunksandmoney : DedicatedServerModInitializer {
	val logger: Logger = LoggerFactory.getLogger("chunks_and_money")
	var db: Connection? = null

	override fun onInitializeServer() {
		// Config
		ConfigManager.loadConfig()

		// Database
		// PostgreSQL
		logger.info("Initializing PostgreSQL database ...")
		val mariadb = Postgresql(
			ConfigManager.config.database.host,
			ConfigManager.config.database.port,
			ConfigManager.config.database.database,
			ConfigManager.config.database.user,
			ConfigManager.config.database.password
		)
		this.db = mariadb.getDatabaseConnection()
		mariadb.setupDatabase(db)
		logger.info("PostgreSQL database initialized")

		// SQLite
		//logger.info("Initializing SQLite database ...")
		//logger.info("SQLite database initialized")

		// Commands
		logger.info("Registering commands ...")
		// Money Commands
		CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
			MoneyCommands(this.db).register(dispatcher)
		})

		// Team Commands
		CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
			TeamCommands(this.db).register(dispatcher)
		})

		// Claim Commands
		CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
			ClaimCommands(this.db).register(dispatcher)
		})
		logger.info("Successfully registered commands")

		// Thinks we want to do, after the server is online
		ServerLifecycleEvents.SERVER_STARTED.register{ _ -> this.onEnable() }

		// If we need to unload stuff
		ServerLifecycleEvents.SERVER_STOPPED.register{ _ -> this.onDisable() }

		ServerPlayConnectionEvents.JOIN.register{ handler, _, _ -> this.onJoin(handler) }
	}

	private fun onEnable() {
		logger.info("Successfully enabled Chunks&Money")

		// Enable PPB
		if (ConfigManager.config.misc.ppb) {
			logger.info("Enabling PlaceholderAPI ProxyBridge")
			val ppb = PapiProxyBridgeManager(this.db)
			ppb.enable()
		}
	}

	private fun onDisable() {
		this.db?.close()
		logger.info("Successfully disabled Chunks&Money")
	}

	private fun onJoin(handler: ServerPlayNetworkHandler) {
		// Check if player is already in database
		 if (PlayerManager.checkIfPlayerExists(handler.player.uuid.toString(), this.db)) {
			 // Update player name (If player changed name)
			 PlayerManager.updateName(handler.player.uuid, handler.player.name.string, this.db)

			 return
		 } else {
			// Create new player in database
			 PlayerManager.createNewPlayer(handler.player.uuid, handler.player.name.string, this.db)
		 }
	}

}