package com.vidbox.telegram

import com.vidbox.album.Album
import com.vidbox.file.File
import com.vidbox.file.FileService
import com.vidbox.keys.KeysService
import com.vidbox.telegram.model.Photo
import com.vidbox.telegram.model.Response
import com.vidbox.telegram.model.Result
import internal.org.springframework.content.rest.controllers.BadRequestException
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.util.stream.Collectors

//TODO: entire class needs major testing
@Service
class TelegramService(@Autowired private val restTemplate: RestTemplate,
                      @Autowired private val fileService: FileService,
                      @Autowired private val keysService: KeysService) {
    companion object {
        val log = LoggerFactory.getLogger(TelegramService::class.java)
    }

    fun postFileToTelegram(file: File, text: String, chatId: String): ResponseEntity<Response> {
        log.debug("telegram request received for file with id: ${file.id}")
        val apiKey = getTelegramApiKey(file)
        val response: ResponseEntity<Response> = getTelegramResult(file, text, apiKey, chatId)
        log.debug("request completed with response: $response")
        return response
    }

    // TODO: proper implementation as documented here: https://core.telegram.org/bots/api#sendmediagroup
    fun postAlbumToTelegram(album: Album, text: String, chatId: String): List<ResponseEntity<Response>> {
        log.debug("telegram request received for album with id: ${album.id}")
        val apiKey = getTelegramApiKey(album)
        return album.files.stream()
            .map { getTelegramResult(it, text, apiKey, chatId) }
            .collect(Collectors.toList())
    }

    private fun getTelegramResult(file: File, text: String, telegramApiKey: String, chatId: String): ResponseEntity<Response> {
        val tmpFile: java.io.File = getTempFile(file)
        val response = when {
            file.mimeType.lowercase().contains("image") -> {
                sendPhoto(text = text,
                    photo = FileSystemResource(tmpFile),
                    telegramApiKey = telegramApiKey,
                    chatId = chatId)
            }
            file.mimeType.lowercase().contains("video") -> {
                sendVideo(text = text,
                    video = FileSystemResource(tmpFile),
                    telegramApiKey = telegramApiKey,
                    chatId = chatId)
            }
            else -> throw BadRequestException("Request cannot be completed")
        }
        deleteTempFile(tmpFile)
        return response
    }

    private fun sendPhoto(text: String,
                  parseMode: String = "HTML",
                  photo: FileSystemResource,
                  telegramApiKey: String,
                  chatId: String): ResponseEntity<Response> {
        log.debug("photo found; sending to telegram")
        val url = "https://api.telegram.org/bot$telegramApiKey/sendPhoto"
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        val body: MultiValueMap<String, Any> = LinkedMultiValueMap()
        body.add("chat_id", chatId)
        body.add("caption", text)
        body.add("parse_mode", parseMode)
        body.add("photo", photo)
        val requestEntity = HttpEntity(body, headers)
        log.debug("Sending to telegram with request: {}", requestEntity)
        log.debug("With url: {}", url)
        return restTemplate.postForEntity(url, requestEntity, Response::class.java)
    }

    // TODO: implement video uploads to telegram
    private fun sendVideo(text: String,
                          parseMode: String = "HTML",
                          video: FileSystemResource,
                          telegramApiKey: String,
                          chatId: String): ResponseEntity<Response> {
        val response = Response(false, Result(Photo("", "", -1, -1)))
    return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    private fun getTempFile(file: File): java.io.File {
        val tmpFile: java.io.File = java.io.File("/tmp/${file.contentId}${file.name}")
        FileUtils.copyInputStreamToFile(fileService.getContentByFile(file), tmpFile)
        log.trace("Temporary file created at: ${tmpFile.name}")
        return tmpFile
    }

    private fun deleteTempFile(file: java.io.File) {
        file.delete()
        log.trace("Temporary file: $file deleted")
    }

    private fun getTelegramApiKey(file: File): String {
        return keysService.getKeys(file.owner).telegramApiKey ?:
        throw BadRequestException("Telegram api key is not set")
    }

    private fun getTelegramApiKey(album: Album): String {
        return keysService.getKeys(album.owner).telegramApiKey ?:
        throw BadRequestException("Telegram api key is not set")
    }
}
