package com.vidbox.controller

import com.vidbox.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
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
    fun getContent(@PathVariable("fileId") id: Long): ResponseEntity<Any> {
        val file = fileService.getFileById(id)
        val inputStreamResource = InputStreamResource(fileService.getContentByFile(file))
        val headers = HttpHeaders()
        headers.set("Content-Type", file.mimeType)
        return ResponseEntity(inputStreamResource, headers, HttpStatus.OK)
    }

    @GetMapping(value = ["/hello"])
    fun hello(principal: Principal): ResponseEntity<String> {
        val response = String.format("Hello, %s", principal.name)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
