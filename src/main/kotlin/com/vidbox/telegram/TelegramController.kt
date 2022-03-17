package com.vidbox.telegram

import com.vidbox.album.AlbumService
import com.vidbox.file.FileService
import com.vidbox.telegram.model.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping(value = ["/telegram"])
class TelegramController(@Autowired private val fileService: FileService,
                         @Autowired private val telegramService: TelegramService,
                         @Autowired private val albumService: AlbumService) {

    @RequestMapping(value = ["/file"], method = [RequestMethod.POST])
    fun postFileToTelegram(@RequestParam fileId: Long,
                           @RequestParam(required = false, defaultValue = "") text: String,
                           @RequestParam chatId: String,
                           principal: Principal): ResponseEntity<Response> {
        val file = fileService.getFileById(principal.name, fileId)
        return telegramService.postFileToTelegram(file = file, text = text, chatId = chatId)
    }

    @RequestMapping(value = ["/album"], method = [RequestMethod.POST])
    fun postAlbumToTelegram(@RequestParam albumId: Long,
                            @RequestParam(required = false, defaultValue = "") text: String,
                            @RequestParam chatId: String,
                            principal: Principal): List<ResponseEntity<Response>> {
        val album = albumService.getAlbum(principal.name, albumId)
        return telegramService.postAlbumToTelegram(album, text, chatId)
    }
}
