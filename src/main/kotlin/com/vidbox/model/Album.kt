package com.vidbox.model

import javax.persistence.*

@Entity
class Album(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long? = null,
            val owner: String,
            @ElementCollection val fileIds: List<Long>)
