package com.vidbox.keys

import org.springframework.beans.factory.annotation.Autowired
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

    @RequestMapping(value = ["/telegramApiKey"], method = [RequestMethod.PUT, RequestMethod.POST])
    fun setTelegramApiKey(principal: Principal, @RequestParam telegramApiKey: String): ResponseEntity<Any> {
        val keys = keysService.setTelegramApiKey(owner = principal.name, telegramApiKey = telegramApiKey)
        return ResponseEntity(keys, HttpStatus.OK)
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun getKeys(principal: Principal): ResponseEntity<Any> {
        return ResponseEntity(keysService.getKeys(principal.name), HttpStatus.OK)
    }
}
