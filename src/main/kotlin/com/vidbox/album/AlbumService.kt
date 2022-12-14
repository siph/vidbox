package com.vidbox.album

import com.vidbox.file.FileService
import com.vidbox.util.validateOwnership
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumService(@Autowired private val albumRepository: AlbumRepository,
                   @Autowired private val fileService: FileService) {
    companion object {
        val log = LoggerFactory.getLogger(AlbumService::class.java)
    }

    fun getAlbums(owner: String, pageRequest: PageRequest): Page<Album> {
        log.debug("get albums request for owner: $owner")
        return albumRepository.findAllByOwner(owner, pageRequest)
    }

    fun getAlbum(owner: String, albumId: Long): Album {
        log.debug("get album request for album with id $albumId")
        val album: Album = albumRepository.findById(albumId)
            .orElseThrow { NotFoundException("Album with id: $albumId not found") }
        validateOwnership(owner, album)
        return album
    }

    fun createAlbum(owner: String): Album {
        log.debug("create album request for owner $owner")
        return saveAlbum(owner = owner, Album(owner = owner))
    }

    fun deleteAlbum(owner: String, albumId: Long) {
        val album = getAlbum(owner, albumId)
        albumRepository.delete(album)
    }

    fun saveAlbum(owner: String, album: Album): Album {
        log.debug("saveAlbum request for owner $owner")
        validateOwnership(owner, album)
        return albumRepository.save(album)
    }

    fun addFileToAlbum(owner: String, albumId: Long, fileId: Long): Album {
        val album = getAlbum(owner, albumId)
        val file = fileService.getFileById(owner, fileId)
        album.files.add(file)
        return saveAlbum(owner, album)
    }

    fun deleteFileFromAlbum(owner: String, albumId: Long, fileId: Long): Album {
        val album = getAlbum(owner, albumId)
        album.files.removeIf { it.id == fileId }
        return saveAlbum(owner, album)
    }
}
