package org.itaya.pixivservice.service

import org.itaya.pixivservice.model.ArtworkFilter
import org.itaya.pixivservice.model.ArtworkInfo

interface ArtworkFilterService {
    fun createFilterConfig(config: (ArtworkFilter) -> Unit): ArtworkFilter
    fun filter(infoList: List<ArtworkInfo>, filter: ArtworkFilter): List<ArtworkInfo>
    fun filter(infoList: List<ArtworkInfo>, config: (ArtworkFilter) -> Unit): List<ArtworkInfo>
}