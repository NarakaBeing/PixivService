package org.itaya.pixivservice.mapper

import org.itaya.pixivservice.model.ArtworkInformationModel

interface PlateBaseArtworkMapper {
    fun selectBySubscriptAuthors(pageCount: Int): List<ArtworkInformationModel>
    fun selectNSFWBySubscriptAuthors(pageCount: Int): List<ArtworkInformationModel>
    fun selectByRecommendedWorks(workCount: Int): List<ArtworkInformationModel>
    fun selectNSFWByRecommendedWorks(workCount: Int): List<ArtworkInformationModel>
    fun selectByRecommendedAuthors(userCount: Int): Map<Int, List<ArtworkInformationModel>>

    fun selectSFWByDailyIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByWeeklyIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByRookieIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByOriginalIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByForMaleIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByForFemaleIntegrationRanking(count: Int): List<ArtworkInformationModel>

    fun selectSFWByDailyIllustrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByWeeklyIllustrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByRookieIllustrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByOriginalIllustrationRanking(count: Int): List<ArtworkInformationModel>

    fun selectSFWByDailyUgoriaRanking(count: Int): List<ArtworkInformationModel>
    fun selectSFWByWeeklyUgoriaRanking(count: Int): List<ArtworkInformationModel>

    fun selectNSFWByDailyIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectNSFWByWeeklyIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectNSFWByForMaleIntegrationRanking(count: Int): List<ArtworkInformationModel>
    fun selectNSFWByForFemaleIntegrationRanking(count: Int): List<ArtworkInformationModel>
}