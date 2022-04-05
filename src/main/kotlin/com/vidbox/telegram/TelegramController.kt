package com.vidbox.telegram

import com.vidbox.album.AlbumService
import com.vidbox.file.FileService
import com.vidbox.keys.Keys
import com.vidbox.telegram.model.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @Operation(summary = "Send file to telegram")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully sent",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Response::class)
            )]
        )
    )
    @RequestMapping(value = ["/file"], method = [RequestMethod.POST])
    fun postFileToTelegram(@RequestParam
                           @Parameter(description = "Id for requested file")
                           fileId: Long,
                           @Parameter(description = "Text to accompany file")
                           @RequestParam(required = false, defaultValue = "")
                           text: String,
                           @RequestParam
                           @Parameter(description = "Id for telegram channel")
                           chatId: String,
                           principal: Principal): ResponseEntity<Response> {
        val file = fileService.getFileById(principal.name, fileId)
        return telegramService.postFileToTelegram(file = file, text = text, chatId = chatId)
    }

    @Operation(summary = "Send album to telegram")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully sent",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Response::class)
            )]
        )
    )
    @RequestMapping(value = ["/album"], method = [RequestMethod.POST])
    fun postAlbumToTelegram(@RequestParam
                            @Parameter(description = "Id for requested album")
                            albumId: Long,
                            @RequestParam(required = false, defaultValue = "")
                            @Parameter(description = "Text to accompany album")
                            text: String,
                            @RequestParam
                            @Parameter(description = "Id for telegram channel")
                            chatId: String,
                            principal: Principal): List<ResponseEntity<Response>> {
        val album = albumService.getAlbum(principal.name, albumId)
        return telegramService.postAlbumToTelegram(album, text, chatId)
    }
}
