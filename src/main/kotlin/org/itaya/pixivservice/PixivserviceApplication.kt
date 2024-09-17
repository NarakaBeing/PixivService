package org.itaya.pixivservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource


@SpringBootApplication
@PropertySource("/config.yaml")
class PixivserviceApplication {
}

fun main(args: Array<String>) {
    val ap = SpringApplication.run(PixivserviceApplication::class.java, *args)
}

