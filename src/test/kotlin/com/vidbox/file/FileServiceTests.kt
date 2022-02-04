package com.vidbox.file

import com.vidbox.util.getMockFile
import internal.org.springframework.content.rest.controllers.BadRequestException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest

@SpringBootTest
class FileServiceTests(@Autowired private val fileService: FileService,
                       @Autowired private val fileRepository: FileRepository,
                       @Autowired private val fileContentStore: FileContentStore) {


    @Test
    fun `assert that file is uploaded`() {
        clearRepository()
        val owner = "test"
        val multipartFile = getMockFile()
        val file = fileService.uploadFile(multipartFile, owner)
        assertThat(file.name).isEqualTo("file")
        assertThat(file.owner).isEqualTo(owner)
        assertThat(file.mimeType).isEqualTo("text/plain")
    }

    @Test
    fun `assert that file is deleted`() {
        clearRepository()
        val owner = "test"
        val multipartFile = getMockFile()
        val file = fileService.uploadFile(multipartFile, owner)
        assertThat(fileService.getFilesByOwner(owner, PageRequest.of(0, 10)).totalElements)
            .isEqualTo(1L)
        fileService.deleteFileById(owner, file.id)
        assertThat(fileService.getFilesByOwner("test", PageRequest.of(0, 10)).totalElements)
            .isEqualTo(0L)
    }

    @Test
    fun `assert that retrieving non owned files fails`() {
        clearRepository()
        val owner = "test"
        val multipartFile = getMockFile()
        val file = fileService.uploadFile(multipartFile, owner)
        assertThatThrownBy { fileService.getFileById("non owner", file.id) }
            .isInstanceOf(BadRequestException::class.java)
    }

    private fun clearRepository() {
        fileRepository.findAll()
            .forEach { fileContentStore.unsetContent(it) }
        fileRepository.deleteAll()
    }
}
