package org.itaya.pixivservice.service.impl

import org.itaya.pixivservice.model.ArtworkFilter
import org.itaya.pixivservice.model.ArtworkFilterImpl
import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.service.ArtworkFilterService
import org.springframework.stereotype.Service

@Service
class ArtworkFilterServiceImpl: ArtworkFilterService{
    override fun createFilterConfig(config: (ArtworkFilter) -> Unit): ArtworkFilter {
        return ArtworkFilterImpl.nullFilter().apply(config)
    }

    override fun filter(
        infoList: List<ArtworkInfo>,
        filter: ArtworkFilter
    ): List<ArtworkInfo> {
        var result: List<ArtworkInfo> = ArrayList<ArtworkInfo>().apply {
            infoList.onEach { add(it) }
        }
        filter.author?.let { standard -> result = result.filter { it.author == standard } }
        filter.dateBefore?.let { standard -> result = result.filter { it.date.before(standard) } }
        filter.dateAfter?.let { standard -> result = result.filter { it.date.after(standard) } }
        filter.includedTags?.let { standard -> result = result.filter { it.tag.intersect(standard.toSet()).isNotEmpty() } }
        filter.excludedTags?.let { standard -> result = result.filter { it.tag.intersect(standard.toSet()).isEmpty() } }
        filter.viewsGreaterThan?.let { standard -> result = result.filter { it.views > standard } }
        filter.viewsSmallerThan?.let { standard -> result = result.filter { it.views < standard } }
        filter.likesGreaterThan?.let { standard -> result = result.filter { it.likes > standard } }
        filter.likesSmallerThan?.let { standard -> result = result.filter { it.likes < standard } }
        filter.bookmarksGreaterThan?.let { standard -> result = result.filter { it.bookmarks > standard } }
        filter.bookmarksSmallerThan?.let { standard -> result = result.filter { it.bookmarks < standard } }
        filter.visibility?.let { standard -> result = result.filter { it.visibility == standard } }
        filter.format?.let { standard -> result = result.filter { it.format == standard } }
        return result
    }

    override fun filter(
        infoList: List<ArtworkInfo>,
        config: (ArtworkFilter) -> Unit
    ): List<ArtworkInfo> {
        return filter(infoList, createFilterConfig(config))
    }
}