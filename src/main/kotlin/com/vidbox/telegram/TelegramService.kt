package com.vidbox.telegram

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vidbox.file.File
import com.vidbox.file.FileService
import com.vidbox.keys.KeysService
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

@Service
class TelegramService(@Autowired private val restTemplate: RestTemplate,
                      @Autowired private val fileService: FileService,
                      @Autowired private val keysService: KeysService) {
    companion object {
        val log = LoggerFactory.getLogger(TelegramService::class.java)
    }

    fun postToTelegram(file: File, text: String, chatId: String): ResponseEntity<Response> {
        val apiKey = keysService.getKeys(file.owner).telegramApiKey ?:
            throw BadRequestException("Telegram api key is not set")
        val tmpFile: java.io.File = getTempFile(file)
        val response: ResponseEntity<Response> = when {
            file.mimeType.contains("image") -> {
                sendPhoto(text = text,
                    photo = FileSystemResource(tmpFile),
                    telegramApiKey = apiKey,
                    chatId = chatId)
            }
            file.mimeType.contains("video") -> {
                sendVideo(text = text,
                    video = FileSystemResource(tmpFile),
                    telegramApiKey = apiKey,
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
    return ResponseEntity(Response(false), HttpStatus.BAD_REQUEST)
    }

    private fun getTempFile(file: File): java.io.File {
        val tmpFile: java.io.File = java.io.File("/tmp/${file.contentId}${file.name}")
        FileUtils.copyInputStreamToFile(fileService.getContentByFile(file), tmpFile)
        return tmpFile
    }

    private fun deleteTempFile(file: java.io.File) {
        file.delete()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(val ok: Boolean)
