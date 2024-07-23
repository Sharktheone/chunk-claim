package de.rgbpixl

import de.rgbpixl.commands.MoneyCommands
import de.rgbpixl.database.MariaDB
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Chunks_and_money : DedicatedServerModInitializer {
	val logger: Logger = LoggerFactory.getLogger("chunks_and_money")

	override fun onInitializeServer() {
		// Database
		logger.info("Initializing Database")
		MariaDB.setupDatabase()
		logger.info("Setuped Database")


		logger.info("Loaded Chunks & Money")

		// Commands
		CommandRegistrationCallback.EVENT.register(MoneyCommands::register)

		// Thinks we want to do, after the server is online
		ServerLifecycleEvents.SERVER_STARTED.register{ server -> this.onEable() }

		// If we need to unload stuff
		ServerLifecycleEvents.SERVER_STOPPED.register{ server -> this.onDisbale() }
	}

	private fun onEable() {
	}

	private fun onDisbale() {
		logger.info("Successfilly disabled Chunks&Money")
	}
}