package com.vidbox.album

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest
class AlbumServiceTests(@Autowired private val albumService: AlbumService) {

    @Test
    fun `assert that albums are created`() {
        albumService.createAlbum("test")
        assertThat(albumService.getAlbums("test", PageRequest.of(1, 10)).totalElements).isEqualTo(1)
    }
}
