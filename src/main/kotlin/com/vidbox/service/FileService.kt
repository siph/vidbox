package com.vidbox.service

import com.vidbox.model.File
import com.vidbox.repository.FileContentStore
import com.vidbox.repository.FileRepository
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.security.Principal

@Service
class FileService(@Autowired private val fileRepository: FileRepository,
                  @Autowired private val fileContentStore: FileContentStore) {

    fun uploadFile(file: MultipartFile, principal: Principal): File{
        val fileData = fileRepository.save(
            File(id = null,
                name = file.name,
                summary = null,
                owner = principal.name,
                contentId = null,
                contentLength = null)
        )
        fileData.mimeType = file.contentType ?: "unknown"
        fileContentStore.setContent(fileData, file.inputStream)
        return fileRepository.save(fileData)
    }

   fun getFileById(id: Long): File {
       return fileRepository.findById(id)
           .orElseThrow { NotFoundException("cannot find file with id: $id") }
   }

   fun getContentByFile(file: File): InputStream {
       return fileContentStore.getContent(file)
   }
}
