package com.vidbox.file

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
class FileContentController(@Autowired private val fileService: FileService) {

    // TODO: restrict file types
    @Operation(summary = "Upload file")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully Uploaded",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = File::class)
            )]
        )
    )
    @RequestMapping(value = ["/upload"], method = [RequestMethod.PUT])
    fun upload(@Parameter(description = "File to be uploaded")
               @RequestParam(name = "file", required = true)
               file: MultipartFile,
               principal: Principal): ResponseEntity<File> {
        return ResponseEntity(fileService.uploadFile(file, principal.name), HttpStatus.OK)
    }

    @Operation(summary = "Get file resource")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = InputStreamResource::class)
            )]
        )
    )
    @RequestMapping(value = ["/files/{fileId}"], method = [RequestMethod.GET])
    fun getContent(@Parameter(description = "Id for requested file")
                   @PathVariable("fileId")
                   id: Long,
                   principal: Principal): ResponseEntity<InputStreamResource> {
        val file = fileService.getFileById(principal.name, id)
        val inputStreamResource = InputStreamResource(fileService.getContentByFile(file))
        val headers = HttpHeaders()
        headers.set("Content-Type", file.mimeType)
        return ResponseEntity(inputStreamResource, headers, HttpStatus.OK)
    }

    @Operation(summary = "Get files")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = File::class)
            )]
        )
    )
    @RequestMapping(value = ["/files"], method = [RequestMethod.GET])
    fun getFiles(@Parameter(description = "Page number")
                 @RequestParam(value = "page", required = false, defaultValue = "1")
                 page: Int,
                 @Parameter(description = "Results per page")
                 @RequestParam(value = "size", required = false, defaultValue = "20")
                 pageSize: Int,
                 principal: Principal): ResponseEntity<Page<File>> {
        return ResponseEntity(
            fileService.getFilesByOwner(principal.name, PageRequest.of(page - 1, pageSize)),
            HttpStatus.OK)
    }

    @Operation(summary = "Delete file")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully removed",
            content = [Content(mediaType = "application/json")]
        )
    )
    @RequestMapping(value = ["/files/{fileId}"], method = [RequestMethod.DELETE])
    fun deleteFile(@Parameter(description = "Id for requested file")
                   @PathVariable("fileId") id: Long,
                   principal: Principal): ResponseEntity<Any> {
        fileService.deleteFileById(principal.name, id)
        return ResponseEntity(HttpStatus.OK)
    }
}
