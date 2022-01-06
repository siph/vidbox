package com.vidbox.file

import com.vidbox.util.validateOwnership
import javassist.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@Service
class FileService(@Autowired private val fileRepository: FileRepository,
                  @Autowired private val fileContentStore: FileContentStore
) {
    companion object {
        val log = LoggerFactory.getLogger(FileService::class.java)
    }

    fun uploadFile(file: MultipartFile, owner: String): File {
        log.debug("upload file request for owner: $owner")
        val fileData = fileRepository.save(
            File(name = file.name,
                owner = owner)
        )
        fileData.mimeType = file.contentType ?: "unknown"
        fileContentStore.setContent(fileData, file.inputStream)
        return fileRepository.save(fileData)
    }

    fun getFileById(owner: String, id: Long): File {
        log.debug("get file request with id: $id")
        val file =  fileRepository.findById(id)
            .orElseThrow { NotFoundException("cannot find file with id: $id") }
        validateOwnership(owner, file)
        return file
    }

    fun getContentByFile(file: File): InputStream {
        log.debug("get content request for file with id: ${file.id}")
        return fileContentStore.getContent(file)
    }

    fun getFilesByOwner(owner: String, pageable: Pageable): Page<File> {
        log.debug("get file list request for owner: $owner")
        log.debug("with pageable: $pageable")
        return fileRepository.findAllByOwner(owner, pageable)
    }

    fun deleteFileById(owner: String, id: Long) {
        log.debug("delete file request for file with id: $id")
        // validated in getFileById() function call
        val file = getFileById(owner, id)
        fileContentStore.unsetContent(file)
        fileRepository.delete(file)
    }

}
