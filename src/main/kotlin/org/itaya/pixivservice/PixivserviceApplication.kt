package org.itaya.pixivservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@PropertySource("/config.yaml")
class PixivserviceApplication