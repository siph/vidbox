package com.vidbox.album

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class AlbumService(@Autowired private val albumRepository: AlbumRepository) {

    fun getAlbums(owner: String, pageRequest: PageRequest): Page<Album> {
        return albumRepository.findAllByOwner(owner, pageRequest)
    }

    fun createAlbum(owner: String): Album {
        val album = Album(owner = owner)
        return albumRepository.save(album)
    }
}
