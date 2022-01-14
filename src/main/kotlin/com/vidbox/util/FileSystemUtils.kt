package com.vidbox.util

import com.vidbox.file.File
import com.vidbox.telegram.TelegramService
import org.apache.commons.io.FileUtils
import java.io.InputStream

class FileSystemUtils

fun getTempFile(file: File, inputStream: InputStream): java.io.File {
    val tmpFile: java.io.File = java.io.File("/tmp/${file.contentId}${file.name}")
    FileUtils.copyInputStreamToFile(inputStream, tmpFile)
    TelegramService.log.trace("Temporary file created at: ${tmpFile.name}")
    return tmpFile
}

fun deleteTempFile(file: java.io.File) {
    file.delete()
    TelegramService.log.trace("Temporary file: $file deleted")
}
