package com.vidbox.file

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name = "files")
class File(@Id @GeneratedValue(strategy = GenerationType.AUTO)
           @Column(name = "id")
           var id: Long? = null,
           @Column(name = "file_name")
           var name: String?,
           @Column(name = "created")
           var created: Date = Date(),
           @Column(name = "owner")
           val owner: String,
           @ContentId
           @Column(name = "content_id")
           var contentId: String? = null,
           @ContentLength
           @Column(name = "content_length")
           var contentLength: Long? = null,
           @Column(name = "mime_type")
           var mimeType: String = "text/plain")
