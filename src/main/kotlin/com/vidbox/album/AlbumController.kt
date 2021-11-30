package com.vidbox.album

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AlbumController(@Autowired private val albumService: AlbumService) {

    @RequestMapping(value = ["/album"], method = [RequestMethod.POST])
    fun createAlbum(principal: Principal): ResponseEntity<Any> {
        return ResponseEntity(albumService.createAlbum(principal), HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/albums"], method = [RequestMethod.GET])
    fun getAlbums(@RequestParam(value = "page", required = false, defaultValue = "1")
                  page: Int,
                  @RequestParam(value = "size", required = false, defaultValue = "20")
                  pageSize: Int,
                  principal: Principal): ResponseEntity<Any> {
        val albums = albumService.getAlbums(principal, PageRequest.of(page, pageSize))
        return ResponseEntity(albums, HttpStatus.OK)
    }
}
