package com.vidbox.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
class FileContentController(@Autowired private val fileService: FileService) {

    @RequestMapping(value = ["/upload"], method = [RequestMethod.PUT])
    fun upload(@RequestParam(name = "file", required = true) file: MultipartFile,
               principal: Principal): ResponseEntity<Any> {
        return ResponseEntity(fileService.uploadFile(file, principal), HttpStatus.OK)
    }

    @RequestMapping(value = ["/files/{fileId}"], method = [RequestMethod.GET])
    fun getContent(@PathVariable("fileId") id: Long, principal: Principal): ResponseEntity<Any> {
        val file = fileService.getFileById(id)
        if (!validateOwnership(principal, file)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }
        val inputStreamResource = InputStreamResource(fileService.getContentByFile(file))
        val headers = HttpHeaders()
        headers.set("Content-Type", file.mimeType)
        return ResponseEntity(inputStreamResource, headers, HttpStatus.OK)
    }

    @RequestMapping(value = ["/files"], method = [RequestMethod.GET])
    fun getFiles(@RequestParam(value = "page", required = false, defaultValue = "1")
                 page: Int,
                 @RequestParam(value = "size", required = false, defaultValue = "20")
                 pageSize: Int,
                 principal: Principal): ResponseEntity<Any> {
        return ResponseEntity(
            fileService.getFilesByOwner(principal.name, PageRequest.of(page - 1, pageSize)),
            HttpStatus.OK)
    }

    @RequestMapping(value = ["/files/{fileId}"], method = [RequestMethod.DELETE])
    fun deleteFile(@PathVariable("fileId") id: Long, principal: Principal): ResponseEntity<Any> {
        val file = fileService.getFileById(id)
        if (!validateOwnership(principal, file)) {
            return ResponseEntity(HttpStatus.FORBIDDEN)
        }
        fileService.deleteFile(file)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping(value = ["/hello"])
    fun hello(principal: Principal): ResponseEntity<String> {
        val response = String.format("Hello, %s", principal.name)
        return ResponseEntity(response, HttpStatus.OK)
    }

    private fun validateOwnership(principal: Principal, file: File): Boolean {
        return principal.name.equals(file.owner)
    }
}
