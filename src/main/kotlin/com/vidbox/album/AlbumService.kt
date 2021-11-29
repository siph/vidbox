package com.vidbox.album

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class AlbumService(@Autowired private val albumRepository: AlbumRepository) {

    fun getAlbums(principal: Principal, pageRequest: PageRequest): Page<Album> {
        return albumRepository.findAllByOwner(principal.name, pageRequest)
    }

    fun createAlbum(principal: Principal): Album {
        val album = Album(owner = principal.name)
        return albumRepository.save(album)
    }
}
