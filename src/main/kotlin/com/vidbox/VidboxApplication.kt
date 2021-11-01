package com.vidbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VidboxApplication

fun main(args: Array<String>) {
    runApplication<VidboxApplication>(*args)
}
