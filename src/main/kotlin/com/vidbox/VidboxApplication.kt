package com.vidbox

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class VidboxApplication{
    companion object {
            val log = LoggerFactory.getLogger(VidboxApplication::class.java)
        }
    @Bean
    fun getRestTemplate(): RestTemplate {
        val restTemplate = RestTemplate()
        log.debug("creating restTemplate bean")
        log.debug("$restTemplate")
        return restTemplate
    }
}

fun main(args: Array<String>) {
    runApplication<VidboxApplication>(*args)
}
