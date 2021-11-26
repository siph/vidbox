package com.vidbox.controller

import com.vidbox.model.Album
import com.vidbox.repository.AlbumRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AlbumController(@Autowired private val albumRepository: AlbumRepository) {

    @RequestMapping(value = ["/album"], method = [RequestMethod.POST])
    fun createAlbum(principal: Principal): ResponseEntity<Any> {
        val album = Album(owner = principal.name)
        return ResponseEntity(albumRepository.save(album), HttpStatus.OK)
    }
}
