package com.vidbox.album

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
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
        return ResponseEntity(albumService.createAlbum(principal.name), HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/albums"], method = [RequestMethod.GET])
    fun getAlbums(@RequestParam(value = "page", required = false, defaultValue = "1")
                  page: Int,
                  @RequestParam(value = "size", required = false, defaultValue = "20")
                  pageSize: Int,
                  principal: Principal): ResponseEntity<Page<Album>> {
        val albums = albumService.getAlbums(principal.name, PageRequest.of(page - 1, pageSize))
        return ResponseEntity(albums, HttpStatus.OK)
    }

    @RequestMapping(value = ["/album/add"], method = [RequestMethod.POST, RequestMethod.PUT])
    fun addFileToAlbum(principal: Principal,
                       @RequestParam(value = "fileId")
                       fileId: Long,
                       @RequestParam(value = "albumId")
                       albumId: Long): ResponseEntity<Album> {
        val album = albumService.addFileToAlbum(owner = principal.name, albumId = albumId, fileId = fileId)
        return ResponseEntity(album, HttpStatus.OK)
    }

    @RequestMapping(value = ["/album/remove"], method = [RequestMethod.POST, RequestMethod.DELETE])
    fun deleteFileFromAlbum(principal: Principal,
                       @RequestParam(value = "fileId")
                       fileId: Long,
                       @RequestParam(value = "albumId")
                       albumId: Long): ResponseEntity<Album> {
        val album = albumService.deleteFileFromAlbum(owner = principal.name, albumId = albumId, fileId = fileId)
        return ResponseEntity(album, HttpStatus.OK)
    }

    @RequestMapping(value = ["/album"], method = [RequestMethod.GET])
    fun getAlbum(principal: Principal,
                 @RequestParam(value = "id")
                 id: Long): ResponseEntity<Album> {
        val album = albumService.getAlbum(owner = principal.name, albumId = id)
        return ResponseEntity(album, HttpStatus.OK)
    }

    @RequestMapping(value = ["/album"], method = [RequestMethod.DELETE])
    fun deleteAlbum(principal: Principal,
                    @RequestParam(value = "id")
                    id: Long): ResponseEntity<Any> {
        albumService.deleteAlbum(owner = principal.name, albumId = id)
        return ResponseEntity(HttpStatus.OK)
    }
}
