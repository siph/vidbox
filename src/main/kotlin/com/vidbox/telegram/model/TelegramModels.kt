package com.vidbox.telegram.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class TelegramModels

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(val ok: Boolean,
                    val result: Result)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(val photo: Photo)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Photo(val file_id: String,
                 val file_size: String,
                 val width: Int,
                 val height: Int)
