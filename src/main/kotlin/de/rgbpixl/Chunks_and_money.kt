package de.rgbpixl

import de.rgbpixl.commands.MoneyCommands
import de.rgbpixl.database.MariaDB
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Chunks_and_money : DedicatedServerModInitializer,
	ServerPlayNetworking.PlayPayloadHandler<ServerMover.ChangeServerPayload> {
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
		ServerLifecycleEvents.SERVER_STARTED.register{ server -> this.onEnable() }

		// If we need to unload stuff
		ServerLifecycleEvents.SERVER_STOPPED.register{ server -> this.onDisable() }


		ServerMover
	}

	private fun onEnable() {
	}

	private fun onDisable() {
		logger.info("Successfilly disabled Chunks&Money")
	}

	override fun receive(payload: ServerMover.ChangeServerPayload?, context: ServerPlayNetworking.Context?) {
	}
}