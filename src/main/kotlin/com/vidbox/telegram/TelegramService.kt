package com.vidbox.telegram

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vidbox.keys.KeysService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class TelegramService(@Autowired private val restTemplate: RestTemplate) {
    companion object {
        val log = LoggerFactory.getLogger(TelegramService::class.java)
    }

    fun sendPhoto(text: String,
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
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(val ok: Boolean)
