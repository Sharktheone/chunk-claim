package com.chunkclaim

import net.fabricmc.api.ModInitializer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Override


class ChunkClaim : ModInitializer {
    private val LOGGER = LoggerFactory.getLogger("ChunkClaim")

    override fun onInitialize() {
        LOGGER.info("ChunkClaim is initializing")
    }
}