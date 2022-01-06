package com.vidbox.album

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest
class AlbumServiceTests(@Autowired private val albumService: AlbumService,
                        @Autowired private val albumRepository: AlbumRepository) {

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
}
