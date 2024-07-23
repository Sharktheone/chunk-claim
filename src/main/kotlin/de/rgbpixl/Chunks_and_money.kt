package de.rgbpixl

import de.rgbpixl.commands.MoneyCommands
import de.rgbpixl.database.Database
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.slf4j.LoggerFactory

object Chunks_and_money : ModInitializer {
    val logger = LoggerFactory.getLogger("chunks_and_money")

	override fun onInitialize() {
		logger.info("Initializing Database")
		Database.setupDatabase()
		logger.info("Setuped Database")

		logger.info("Loaded Chunks & Money")

		CommandRegistrationCallback.EVENT.register(MoneyCommands::register)
	}
}