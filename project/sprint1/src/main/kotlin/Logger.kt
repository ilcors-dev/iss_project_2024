package main.kotlin

import unibo.basicomm23.utils.CommUtils
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.FileWriter

class Logger(private val logFileName: String) {

    private val logFile: File = File(logFileName)

    init {
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                throw RuntimeException("Failed to create log file: $logFileName", e)
            }
        }
    }
	
    companion object {
        private val instances = mutableMapOf<String, Logger>()

        fun getInstance(logFileName: String): Logger {
            return synchronized(this) {
                instances.getOrPut(logFileName) { Logger(logFileName) }
            }
        }
    }

    @Synchronized
    fun log(message: String) {
        try {
            FileWriter(logFile, true).use { writer ->
                val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                writer.appendLine("[$timestamp]:$message")
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to write to log file: $logFileName", e)
        }
    }

    @Synchronized
    fun logError(errorMessage: String) {
        log("ERROR: $errorMessage")
    }

    @Synchronized
    fun logInfo(infoMessage: String) {
        log("INFO: $infoMessage")
    }
}
