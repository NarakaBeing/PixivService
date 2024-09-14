package org.itaya.pixivservice

import org.itaya.pixivservice.controller.ControllerA
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource


@SpringBootApplication
@PropertySource("/config.yaml")
class PixivserviceApplication {
}

fun main(args: Array<String>) {
    val ap = SpringApplication.run(PixivserviceApplication::class.java, *args)
    ap.getBean(ControllerA::class.java).main()
}

