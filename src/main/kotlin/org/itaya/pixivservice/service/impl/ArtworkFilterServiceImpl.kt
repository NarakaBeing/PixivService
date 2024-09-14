package org.itaya.pixivservice.service.impl

import cn.hutool.core.util.ArrayUtil
import org.itaya.pixivservice.model.ArtworkFilterConfig
import org.itaya.pixivservice.model.ArtworkFilterConfigImpl
import org.itaya.pixivservice.model.ArtworkInformationModel
import org.itaya.pixivservice.service.ArtworkFilterService
import org.springframework.stereotype.Service

@Service
class ArtworkFilterServiceImpl: ArtworkFilterService{
    override fun createFilterConfig(config: (ArtworkFilterConfig) -> Unit): ArtworkFilterConfig {
        return ArtworkFilterConfigImpl().apply(config)
    }

    override fun filter(
        artworkInformationModelList: List<ArtworkInformationModel>,
        config: ArtworkFilterConfig
    ): List<ArtworkInformationModel> {
        val result = ArrayUtil.clone(artworkInformationModelList)
        config.id?.let { standard -> result.filter { it.id == standard } }
        config.title?.let { standard -> result.filter { it.title == standard } }
        config.author?.let { standard -> result.filter { it.author == standard } }
        config.dateBefore?.let { standard -> result.filter { it.date.before(standard) } }
        config.dateAfter?.let { standard -> result.filter { it.date.after(standard) } }
        config.includedTags?.let { standard -> result.filter { it.tag.intersect(standard.toSet()).isNotEmpty() } }
        config.excludedTags?.let { standard -> result.filter { it.tag.intersect(standard.toSet()).isEmpty() } }
        config.viewsGreaterThan?.let { standard -> result.filter { it.views > standard } }
        config.viewsSmallerThan?.let { standard -> result.filter { it.views < standard } }
        config.likesGreaterThan?.let { standard -> result.filter { it.likes > standard } }
        config.likesSmallerThan?.let { standard -> result.filter { it.likes < standard } }
        config.bookmarksGreaterThan?.let { standard -> result.filter { it.bookmarks > standard } }
        config.bookmarksSmallerThan?.let { standard -> result.filter { it.bookmarks < standard } }
        config.visibility?.let { standard -> result.filter { it.visibility == standard } }
        config.format?.let { standard -> result.filter { it.format == standard } }
        return result
    }

    override fun filter(
        artworkInformationModelList: List<ArtworkInformationModel>,
        config: (ArtworkFilterConfig) -> Unit
    ): List<ArtworkInformationModel> {
        return filter(artworkInformationModelList, createFilterConfig(config))
    }
}