package com.vidbox.telegram

import com.vidbox.file.FileService
import com.vidbox.file.validateOwnership
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
    fun postFileToTelegram(@RequestParam fileId: Long, principal: Principal): ResponseEntity<Any> {
        val file = fileService.getFileById(fileId)
        if (!validateOwnership(principal, file)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }
}
