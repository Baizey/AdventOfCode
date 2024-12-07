package utils

import java.io.File
import java.util.*

object Config {
    private var instance: Properties? = null
    fun get(): Properties {
        if (instance != null) return instance!!
        val properties = Properties()
        File("config.properties").inputStream().use(properties::load)
        instance = properties
        return properties
    }

}