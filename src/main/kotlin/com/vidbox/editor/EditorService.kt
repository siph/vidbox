package com.vidbox.editor

import com.vidbox.album.Album
import com.vidbox.album.AlbumService
import com.vidbox.file.File
import com.vidbox.file.FileService
import com.vidbox.util.deleteTempFile
import com.vidbox.util.getTempFile
import org.apache.commons.fileupload.disk.DiskFileItem
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartFile
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.annotation.PostConstruct

//@Service
class EditorService(@Autowired private val albumService: AlbumService,
                    @Autowired private val fileService: FileService) {
    companion object {
        val log = LoggerFactory.getLogger(EditorService::class.java)
    }

    @PostConstruct
    fun init() {
        validateClippit()
        validateCattit()
    }

    //TODO: make and delete directory
    private fun cattitAlbum(album: Album, newFileName: String): File {
        val rootPath = "/tmp/${album.owner}"
        val files: List<java.io.File> = album.files.stream()
            .map { getTempFile(it, fileService.getContentByFile(it), rootPath) }
            .collect(Collectors.toList())
        getProcessBuilder(listOf("cattit", "-i", rootPath, "-t", newFileName)).start()
        val newFile = fileService.uploadFile(
            getMultipartFile(java.io.File("$rootPath/$newFileName")),
            album.owner)
        files.forEach { deleteTempFile(it) }
        return newFile
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

    private fun getMultipartFile(file: java.io.File): MultipartFile {
        val fileItem = DiskFileItem(
            "file",
            Files.probeContentType(file.toPath()),
            false,
            file.name,
            file.length().toInt(),
            file.parentFile)
        val inputStream = FileInputStream(file)
        val outputStream = fileItem.outputStream
        IOUtils.copy(inputStream, outputStream)
        return CommonsMultipartFile(fileItem)
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
