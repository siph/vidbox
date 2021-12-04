package com.vidbox.file

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile

@SpringBootTest
class FileServiceTests(@Autowired private val fileService: FileService) {

    @Test
    fun `assert that file is uploaded`() {
        val file = MockMultipartFile("file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "testing".encodeToByteArray())
        val result = fileService.uploadFile(file, "test")
        assertThat(result.name).isEqualTo("file")
        assertThat(result.owner).isEqualTo("test")
        assertThat(result.mimeType).isEqualTo("text/plain")
    }
}
