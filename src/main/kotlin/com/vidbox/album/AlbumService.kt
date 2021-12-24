package com.vidbox.album

import internal.org.springframework.content.rest.controllers.BadRequestException
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumService(@Autowired private val albumRepository: AlbumRepository) {
    companion object {
        val log = LoggerFactory.getLogger(AlbumService::class.java)
    }

    fun getAlbums(owner: String, pageRequest: PageRequest): Page<Album> {
        log.debug("get albums request for owner: $owner")
        return albumRepository.findAllByOwner(owner, pageRequest)
    }

    fun createAlbum(owner: String): Album {
        log.debug("create album request for owner $owner")
        val album = Album(owner = owner)
        return albumRepository.save(album)
    }

    fun deleteAlbum(owner: String, albumId: Long) {
        val album: Album = albumRepository.findById(albumId)
            .orElseThrow { NotFoundException("Album with id: $albumId not found") }
        if (!album.owner.equals(owner)) {
            throw BadRequestException("Request not from owner")
        }
        albumRepository.delete(album)
    }

}
