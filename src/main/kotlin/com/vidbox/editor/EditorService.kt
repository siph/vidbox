package com.vidbox.editor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Service
class EditorService {
    companion object {
        val log = LoggerFactory.getLogger(EditorService::class.java)
    }

    @PostConstruct
    fun init() {
        validateClippit()
        validateCattit()
    }

    private fun validateCattit(): Boolean {
        val processBuilder = getProcessBuilder(listOf("cattit", "--version"))
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return when (reader.readLine().contains("cattit", ignoreCase = true)) {
            true -> {
                log.info("cattit found")
                true }
            false -> {
                log.error("cattit not on \$PATH")
                log.error("cattit can be found here: https://gitlab.com/xsiph/cattit")
                false }
        }
    }

    private fun validateClippit(): Boolean {
        val processBuilder = getProcessBuilder(listOf("clippit", "--version"))
        val process = processBuilder.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return when (reader.readLine().contains("clippit", ignoreCase = true)) {
            true -> {
                log.info("clippit found")
                true }
            false -> {
                log.error("clippit not on \$PATH")
                log.error("clippit can be found here: https://gitlab.com/xsiph/clippit")
                false }
        }
    }

    private fun getProcessBuilder(command: List<String>): ProcessBuilder {
        val processBuilder = ProcessBuilder()
        processBuilder.command(command)
        return processBuilder
    }

}

class StreamGobbler(private val inputStream: InputStream,
                    private val consumer: Consumer<String>): Runnable {
    override fun run() {
        BufferedReader(InputStreamReader(inputStream))
            .lines()
            .forEach(consumer)
    }
}
