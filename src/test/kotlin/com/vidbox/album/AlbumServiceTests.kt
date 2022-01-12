package com.vidbox.album

import com.vidbox.file.FileService
import com.vidbox.util.getMockFile
import internal.org.springframework.content.rest.controllers.BadRequestException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest
class AlbumServiceTests(@Autowired private val albumService: AlbumService,
                        @Autowired private val albumRepository: AlbumRepository,
                        @Autowired private val fileService: FileService) {

    val owner = "test"

    @AfterEach
    fun `clear repository`() {
        albumRepository.deleteAll()
    }

    @Test
    fun `assert that album is created and retrieved`() {
        val album = albumService.createAlbum(owner)
        assertThat(albumService.getAlbum(album.owner, album.id))
            .usingRecursiveComparison().isEqualTo(album)
    }

    @Test
    fun `assert that list is retrieved and album is deleted`() {
        albumService.createAlbum(owner)
        val albums = albumService.getAlbums(owner, PageRequest.of(0, 10))
        assertThat(albums.totalElements).isEqualTo(1)
        val album = albums.get().findFirst().orElseThrow { Exception() }
        albumService.deleteAlbum(album.owner, album.id)
        assertThat(albumService.getAlbums(owner, PageRequest.of(1, 10)).totalElements).isEqualTo(0)
    }

    @Test
    fun `assert that retrieving non owned album fails`() {
        val album = albumService.createAlbum(owner)
        assertThatThrownBy { albumService.getAlbum("not owner", album.id) }
            .isInstanceOf(BadRequestException::class.java)
    }

    @Test
    fun `assert that file is added to and removed from album`() {
        val file = fileService.uploadFile(getMockFile(), owner)
        var album = albumService.createAlbum(owner)
        albumService.addFileToAlbum(owner = owner, fileId = file.id, albumId = album.id)
        assertThat(albumService.getAlbum(owner, album.id).files.size).isEqualTo(1)
        album = albumService.deleteFileFromAlbum(owner = owner, albumId = album.id, fileId = file.id)
        assertThat(albumService.getAlbum(owner, album.id).files.size).isEqualTo(0)
    }
}
