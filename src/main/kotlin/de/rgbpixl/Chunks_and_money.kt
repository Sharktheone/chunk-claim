package de.rgbpixl

import de.rgbpixl.commands.MoneyCommands
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import org.slf4j.LoggerFactory

object Chunks_and_money : ModInitializer {
    private val logger = LoggerFactory.getLogger("chunks_and_money")

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")

		CommandRegistrationCallback.EVENT.register(MoneyCommands::register)
	}
}