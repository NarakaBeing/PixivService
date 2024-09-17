package org.itaya.pixivservice.mapper

import org.itaya.pixivservice.model.ArtworkInfo

interface PlateBaseArtworkMapper {
    fun selectBySubscriptAuthors(count: Int): List<ArtworkInfo>
    fun selectNSFWBySubscriptAuthors(count: Int): List<ArtworkInfo>
    fun selectByRecommendedWorks(count: Int): List<ArtworkInfo>
    fun selectNSFWByRecommendedWorks(count: Int): List<ArtworkInfo>
    fun selectByRecommendedAuthors(userCount: Int): Map<Int, List<ArtworkInfo>>

    fun selectSFWByDailyIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByWeeklyIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByRookieIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByOriginalIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByForMaleIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByForFemaleIntegrationRanking(count: Int): List<ArtworkInfo>

    fun selectSFWByDailyIllustrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByWeeklyIllustrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByRookieIllustrationRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByOriginalIllustrationRanking(count: Int): List<ArtworkInfo>

    fun selectSFWByDailyUgoriaRanking(count: Int): List<ArtworkInfo>
    fun selectSFWByWeeklyUgoriaRanking(count: Int): List<ArtworkInfo>

    fun selectNSFWByDailyIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectNSFWByWeeklyIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectNSFWByForMaleIntegrationRanking(count: Int): List<ArtworkInfo>
    fun selectNSFWByForFemaleIntegrationRanking(count: Int): List<ArtworkInfo>
}