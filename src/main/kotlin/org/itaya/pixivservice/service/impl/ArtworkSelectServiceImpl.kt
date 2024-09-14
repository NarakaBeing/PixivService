package org.itaya.pixivservice.service.impl

import cn.hutool.core.util.ArrayUtil
import org.itaya.pixivservice.mapper.ArtworkFileMapper
import org.itaya.pixivservice.mapper.CustomBaseArtworkMapper
import org.itaya.pixivservice.mapper.PlateBaseArtworkMapper
import org.itaya.pixivservice.model.ArtworkInformationModel
import org.itaya.pixivservice.service.ArtworkSelectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class ArtworkSelectServiceImpl @Autowired constructor(
    val customBaseArtworkMapper: CustomBaseArtworkMapper,
    val plateBaseArtworkMapper: PlateBaseArtworkMapper,
    val artworkFileMapper: ArtworkFileMapper
): ArtworkSelectService {
    class ArtworkID(
        private val customBaseArtworkMapper: CustomBaseArtworkMapper,
        private val plateBaseArtworkMapper: PlateBaseArtworkMapper
    ): ArtworkSelectService.ArtworkID {
        override fun select(id: Int): ArtworkInformationModel {
            return customBaseArtworkMapper.selectById(id);
        }

        override fun selectRecommended(workCount: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectByRecommendedWorks(workCount)
        }

        override fun selectRecommendedNSFW(workCount: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectNSFWByRecommendedWorks(workCount)
        }
    }
    
    class Author(
        private val customBaseArtworkMapper: CustomBaseArtworkMapper,
        private val plateBaseArtworkMapper: PlateBaseArtworkMapper
    ): ArtworkSelectService.Author {
        override fun select(name: String): List<ArtworkInformationModel> {
            return customBaseArtworkMapper.selectByAuthor(name);
        }

        override fun select(id: Int): List<ArtworkInformationModel> {
            return customBaseArtworkMapper.selectByAuthor(id);
        }

        override fun selectSubscribed(pageCount: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectBySubscriptAuthors(pageCount)
        }

        override fun selectSubscribedNSFW(pageCount: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectNSFWBySubscriptAuthors(pageCount)
        }

        override fun selectRecommended(userCount: Int): Map<Int, List<ArtworkInformationModel>> {
            return plateBaseArtworkMapper.selectByRecommendedAuthors(userCount)
        }
    }

    class Title(val customBaseArtworkMapper: CustomBaseArtworkMapper): ArtworkSelectService.Title {
        override fun select(name: String): ArtworkInformationModel {
            return customBaseArtworkMapper.selectByTitle(name)
        }
    }

    class Tags(val customBaseArtworkMapper: CustomBaseArtworkMapper): ArtworkSelectService.Tags {
        override fun select(tags: List<String>): List<ArtworkInformationModel> {
            return customBaseArtworkMapper.selectByTags(tags);
        }
    }

    class Ranking(
        private val plateBaseArtworkMapper: PlateBaseArtworkMapper
    ): ArtworkSelectService.Ranking {
        override fun selectSFWDailyIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByDailyIntegrationRanking(count)
        }

        override fun selectSFWWeeklyIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByWeeklyIntegrationRanking(count)
        }

        override fun selectSFWRookieIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByRookieIntegrationRanking(count)
        }

        override fun selectSFWOriginalIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByOriginalIntegrationRanking(count)
        }

        override fun selectSFWForMaleIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByForMaleIntegrationRanking(count)
        }

        override fun selectSFWForFemaleIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectNSFWByForFemaleIntegrationRanking(count)
        }

        override fun selectSFWDailyIllustration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByDailyIllustrationRanking(count)
        }

        override fun selectSFWWeeklyIllustration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByWeeklyIllustrationRanking(count)
        }

        override fun selectSFWRookieIllustration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByRookieIllustrationRanking(count)
        }

        override fun selectSFWOriginalIllustration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByOriginalIllustrationRanking(count)
        }

        override fun selectSFWDailyUgoria(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByDailyUgoriaRanking(count)
        }

        override fun selectSFWWeeklyUgoria(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectSFWByWeeklyUgoriaRanking(count)
        }

        override fun selectNSFWDailyIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectNSFWByDailyIntegrationRanking(count)
        }

        override fun selectNSFWWeeklyIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectNSFWByWeeklyIntegrationRanking(count)
        }

        override fun selectNSFWForMaleIntegration(count: Int): List<ArtworkInformationModel> {
            return plateBaseArtworkMapper.selectNSFWByForMaleIntegrationRanking(count)
        }

        override fun selectNSFWForFemaleIntegration(count: Int): List<ArtworkInformationModel> {
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