package com.vidbox.telegram

import com.vidbox.file.FileService
import com.vidbox.util.validateOwnership
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class TelegramController(@Autowired private val fileService: FileService,
                         @Autowired private val telegramService: TelegramService) {

    @RequestMapping(value = ["/telegram"], method = [RequestMethod.POST])
    fun postFileToTelegram(@RequestParam fileId: Long,
                           @RequestParam(required = false, defaultValue = "") text: String,
                           @RequestParam chatId: String,
                           principal: Principal): ResponseEntity<*> {
        val file = fileService.getFileById(fileId)
        if (!validateOwnership(principal.name, file)) {
            return ResponseEntity(null, HttpStatus.FORBIDDEN)
        }
        return telegramService.postToTelegram(file = file, text = text, chatId = chatId)
    }
}
