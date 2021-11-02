package com.vidbox.controller

import com.vidbox.repository.FileContentStore
import com.vidbox.repository.FileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class FileContentController(@Autowired private val fileRepository: FileRepository,
                            @Autowired private val fileContentStore: FileContentStore) {

    @RequestMapping(value = ["/files/{fileId}"], method = [RequestMethod.PUT])
    fun setContent(@PathVariable("fileId") id: Long,
                   @RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        val f = fileRepository.findById(id)
        if (f.isPresent) {
            f.get().mimeType = file.contentType ?: "unknown"
            fileContentStore.setContent(f.get(), file.inputStream)
            fileRepository.save(f.get())
            return ResponseEntity(HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @RequestMapping(value = ["/files/{fileId}"], method = [RequestMethod.GET])
    fun getContent(@PathVariable("fileId") id: Long): ResponseEntity<Any> {
        val f = fileRepository.findById(id)
        if (f.isPresent) {
            val inputStreamResource = InputStreamResource(fileContentStore.getContent(f.get()))
            val headers = HttpHeaders()
            headers.set("Content-Type", f.get().mimeType)
            return ResponseEntity(inputStreamResource, headers, HttpStatus.OK)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}
