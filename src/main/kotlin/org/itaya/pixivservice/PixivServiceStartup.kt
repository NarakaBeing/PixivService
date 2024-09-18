package org.itaya.pixivservice

import org.itaya.pixivservice.service.ArtworkDownloadService
import org.itaya.pixivservice.service.ArtworkFilterService
import org.itaya.pixivservice.service.ArtworkSelectService
import org.itaya.pixivservice.utils.HttpRequest
import org.springframework.boot.SpringApplication

class PixivServiceStartup {
    companion object {
        private val application = SpringApplication.run(PixivserviceApplication::class.java)
        private val artworkSelectService: ArtworkSelectService = application.getBean(ArtworkSelectService::class.java)
        private val artworkFilterService: ArtworkFilterService = application.getBean(ArtworkFilterService::class.java)
        private val artworkDownloadService: ArtworkDownloadService = application.getBean(ArtworkDownloadService::class.java)

        fun setCookie(string: String) {
            HttpRequest.cookie = string
        }

        fun startup(action: (ArtworkSelectService, ArtworkFilterService, ArtworkDownloadService) -> Unit) {
            action.invoke(artworkSelectService, artworkFilterService, artworkDownloadService)
        }
    }
}