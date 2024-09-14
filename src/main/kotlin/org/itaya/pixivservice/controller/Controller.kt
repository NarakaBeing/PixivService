package org.itaya.pixivservice.controller

import org.itaya.pixivservice.mapper.ArtworkFileMapper
import org.itaya.pixivservice.model.ArtworkInformationModel
import org.itaya.pixivservice.service.ArtworkDownloadService
import org.itaya.pixivservice.service.ArtworkFilterService
import org.itaya.pixivservice.service.ArtworkSelectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class ControllerA @Autowired constructor(
    val artworkSelectService: ArtworkSelectService,
    val artworkDownloadService: ArtworkDownloadService,
    val artworkFilterService: ArtworkFilterService
)  {
    fun main() {
        val idList = ArrayList<ArtworkInformationModel>()
        artworkSelectService.selectionBaseOnArtworkID {
            idList.addAll(this.selectRecommended(100));
            idList.addAll(this.selectRecommendedNSFW(100))
        }
        artworkSelectService.selectionBaseOnAuthor {
            idList.addAll(this.selectSubscribed(50))
        }
        artworkFilterService.filter(idList) {
            it.bookmarksGreaterThan = 3000
            it.likesGreaterThan = 3000
        }
        artworkDownloadService.downloadArtworkAsFile(idList)
    }
}