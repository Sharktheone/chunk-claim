package de.rgbpixl.utils

import net.william278.papiproxybridge.api.PlaceholderAPI
import java.sql.Connection

class PapiProxyBridgeManager(private val db: Connection?) {
    private val api = PlaceholderAPI.createInstance()

    fun enable() {

    }
}