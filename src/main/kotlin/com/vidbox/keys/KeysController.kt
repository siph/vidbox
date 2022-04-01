package com.vidbox.keys

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("keys")
class KeysController(@Autowired private val keysService: KeysService) {

    @Operation(summary = "Set telegram api-key")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully added",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Keys::class)
            )]
        )
    )
    @RequestMapping(value = ["/telegramApiKey"], method = [RequestMethod.PUT, RequestMethod.POST])
    fun setTelegramApiKey(principal: Principal,
                          @Parameter(description = "Telegram api-key")
                          @RequestParam(required = false)
                          telegramApiKey: String?): ResponseEntity<Keys> {
        val keys = keysService.setTelegramApiKey(owner = principal.name, telegramApiKey = telegramApiKey)
        return ResponseEntity(keys, HttpStatus.OK)
    }

    @Operation(summary = "Get all api-keys")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Keys::class)
            )]
        )
    )
    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun getKeys(principal: Principal): ResponseEntity<Keys> {
        return ResponseEntity(keysService.getKeys(principal.name), HttpStatus.OK)
    }
}
