package com.vidbox.album

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @Operation(summary = "Create empty album")
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Album created",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Album::class))]
        )
    )
    @RequestMapping(value = ["/album"], method = [RequestMethod.POST])
    fun createAlbum(principal: Principal): ResponseEntity<Album> {
        return ResponseEntity(albumService.createAlbum(principal.name), HttpStatus.CREATED)
    }

    @Operation(summary = "Get users albums")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Albums retrieved",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Album::class))]
        )
    )
    @RequestMapping(value = ["/albums"], method = [RequestMethod.GET])
    fun getAlbums(@Parameter(description = "Page number")
                  @RequestParam(value = "page", required = false, defaultValue = "1")
                  page: Int,
                  @Parameter(description = "Results per page")
                  @RequestParam(value = "size", required = false, defaultValue = "20")
                  pageSize: Int,
                  principal: Principal): ResponseEntity<Page<Album>> {
        val albums = albumService.getAlbums(principal.name, PageRequest.of(page - 1, pageSize))
        return ResponseEntity(albums, HttpStatus.OK)
    }

    @Operation(summary = "Add file to album")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully added",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Album::class))]
        )
    )
    @RequestMapping(value = ["/album/add"], method = [RequestMethod.POST, RequestMethod.PUT])
    fun addFileToAlbum(principal: Principal,
                       @Parameter(description = "Id for requested file")
                       @RequestParam(value = "fileId")
                       fileId: Long,
                       @Parameter(description = "Id for requested album")
                       @RequestParam(value = "albumId")
                       albumId: Long): ResponseEntity<Album> {
        val album = albumService.addFileToAlbum(owner = principal.name, albumId = albumId, fileId = fileId)
        return ResponseEntity(album, HttpStatus.OK)
    }

    @Operation(summary = "Remove file from album")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully removed",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Album::class))]
        )
    )
    @RequestMapping(value = ["/album/remove"], method = [RequestMethod.POST, RequestMethod.DELETE])
    fun deleteFileFromAlbum(principal: Principal,
                            @Parameter(description = "Id for requested file")
                            @RequestParam(value = "fileId")
                            fileId: Long,
                            @Parameter(description = "Id for requested album")
                            @RequestParam(value = "albumId")
                            albumId: Long): ResponseEntity<Album> {
        val album = albumService.deleteFileFromAlbum(owner = principal.name, albumId = albumId, fileId = fileId)
        return ResponseEntity(album, HttpStatus.OK)
    }

    @Operation(summary = "Get album")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Album::class))]
        )
    )
    @RequestMapping(value = ["/album"], method = [RequestMethod.GET])
    fun getAlbum(principal: Principal,
                 @Parameter(description = "Id for requested album")
                 @RequestParam(value = "id")
                 id: Long): ResponseEntity<Album> {
        val album = albumService.getAlbum(owner = principal.name, albumId = id)
        return ResponseEntity(album, HttpStatus.OK)
    }

    @Operation(summary = "Delete album")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully deleted",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = Album::class))]
        )
    )
    @RequestMapping(value = ["/album"], method = [RequestMethod.DELETE])
    fun deleteAlbum(principal: Principal,
                    @Parameter(description = "Id for requested album")
                    @RequestParam(value = "id")
                    id: Long): ResponseEntity<Any> {
        albumService.deleteAlbum(owner = principal.name, albumId = id)
        return ResponseEntity(HttpStatus.OK)
    }
}
