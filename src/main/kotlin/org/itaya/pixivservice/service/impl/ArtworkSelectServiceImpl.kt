package org.itaya.pixivservice.service.impl

import org.itaya.pixivservice.mapper.CustomBaseArtworkMapper
import org.itaya.pixivservice.mapper.PlateBaseArtworkMapper
import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.service.ArtworkSelectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArtworkSelectServiceImpl @Autowired constructor(
    val customBaseArtworkMapper: CustomBaseArtworkMapper,
    val plateBaseArtworkMapper: PlateBaseArtworkMapper,
): ArtworkSelectService {
    class ArtworkID(
        private val customBaseArtworkMapper: CustomBaseArtworkMapper,
        private val plateBaseArtworkMapper: PlateBaseArtworkMapper
    ): ArtworkSelectService.ArtworkID {
        override fun select(id: Int): ArtworkInfo {
            return customBaseArtworkMapper.selectById(id);
        }

        override fun selectRecommended(workCount: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectByRecommendedWorks(workCount)
        }

        override fun selectRecommendedNSFW(workCount: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectNSFWByRecommendedWorks(workCount)
        }
    }
    
    class Author(
        private val customBaseArtworkMapper: CustomBaseArtworkMapper,
        private val plateBaseArtworkMapper: PlateBaseArtworkMapper
    ): ArtworkSelectService.Author {
        override fun select(name: String, count: Int): List<ArtworkInfo> {
            return customBaseArtworkMapper.selectByAuthor(name, count);
        }

        override fun select(id: Int, count: Int): List<ArtworkInfo> {
            return customBaseArtworkMapper.selectByAuthor(id, count);
        }

        override fun selectSubscribed(pageCount: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectBySubscriptAuthors(pageCount)
        }

        override fun selectSubscribedNSFW(pageCount: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectNSFWBySubscriptAuthors(pageCount)
        }

        override fun selectRecommended(userCount: Int): Map<Int, List<ArtworkInfo>> {
            return plateBaseArtworkMapper.selectByRecommendedAuthors(userCount)
        }
    }

    class Title(private val customBaseArtworkMapper: CustomBaseArtworkMapper): ArtworkSelectService.Title {
        override fun select(name: String, count: Int): ArtworkInfo {
            return customBaseArtworkMapper.selectByTitle(name, count)
        }
    }

    class Tags(private val customBaseArtworkMapper: CustomBaseArtworkMapper): ArtworkSelectService.Tags {
        override fun select(tags: List<String>, count: Int): List<ArtworkInfo> {
            return customBaseArtworkMapper.selectByTags(tags, count);
        }
    }

    class Ranking(
        private val plateBaseArtworkMapper: PlateBaseArtworkMapper
    ): ArtworkSelectService.Ranking {
        override fun selectSFWDailyIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByDailyIntegrationRanking(count)
        }

        override fun selectSFWWeeklyIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByWeeklyIntegrationRanking(count)
        }

        override fun selectSFWRookieIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByRookieIntegrationRanking(count)
        }

        override fun selectSFWOriginalIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByOriginalIntegrationRanking(count)
        }

        override fun selectSFWForMaleIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByForMaleIntegrationRanking(count)
        }

        override fun selectSFWForFemaleIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectNSFWByForFemaleIntegrationRanking(count)
        }

        override fun selectSFWDailyIllustration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByDailyIllustrationRanking(count)
        }

        override fun selectSFWWeeklyIllustration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByWeeklyIllustrationRanking(count)
        }

        override fun selectSFWRookieIllustration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByRookieIllustrationRanking(count)
        }

        override fun selectSFWOriginalIllustration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByOriginalIllustrationRanking(count)
        }

        override fun selectSFWDailyUgoria(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByDailyUgoriaRanking(count)
        }

        override fun selectSFWWeeklyUgoria(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectSFWByWeeklyUgoriaRanking(count)
        }

        override fun selectNSFWDailyIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectNSFWByDailyIntegrationRanking(count)
        }

        override fun selectNSFWWeeklyIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectNSFWByWeeklyIntegrationRanking(count)
        }

        override fun selectNSFWForMaleIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectNSFWByForMaleIntegrationRanking(count)
        }

        override fun selectNSFWForFemaleIntegration(count: Int): List<ArtworkInfo> {
            return plateBaseArtworkMapper.selectNSFWByForFemaleIntegrationRanking(count)
        }
    }

    override fun selectionBaseOnArtworkID(block: ArtworkSelectService.ArtworkID.() -> Unit) {
        ArtworkID(customBaseArtworkMapper, plateBaseArtworkMapper).apply(block)
    }

    override fun selectionBaseOnAuthor(block: ArtworkSelectService.Author.() -> Unit) {
        Author(customBaseArtworkMapper, plateBaseArtworkMapper).apply(block)
    }

    override fun selectionBaseOnTitle(block: ArtworkSelectService.Title.() -> Unit) {
        Title(customBaseArtworkMapper).apply(block)
    }

    override fun selectionBaseOnTags(block: ArtworkSelectService.Tags.() -> Unit) {
        Tags(customBaseArtworkMapper).apply(block)
    }

    override fun selectionBaseOnRanking(block: ArtworkSelectService.Ranking.() -> Unit) {
        Ranking(plateBaseArtworkMapper).apply(block)
    }
}