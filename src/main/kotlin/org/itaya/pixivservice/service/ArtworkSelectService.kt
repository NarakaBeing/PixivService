package org.itaya.pixivservice.service

import org.itaya.pixivservice.model.ArtworkInfo

interface ArtworkSelectService {
    interface ArtworkID {
        fun select(id: Int): ArtworkInfo
        fun selectRecommended(workCount: Int): List<ArtworkInfo>
        fun selectRecommendedNSFW(workCount: Int): List<ArtworkInfo>
    }
    
    interface Author {
        fun selectSubscribed(pageCount: Int): List<ArtworkInfo>
        fun selectSubscribedNSFW(pageCount: Int): List<ArtworkInfo>
        fun selectRecommended(userCount: Int): Map<Int, List<ArtworkInfo>>
        fun select(name: String, count: Int): List<ArtworkInfo>
        fun select(id: Int, count: Int): List<ArtworkInfo>
    }
    
    interface Title {
        fun select(name: String, count: Int): ArtworkInfo
    }
    
    interface Tags {
        fun select(tags: List<String>, count: Int): List<ArtworkInfo>
    }
    
    interface Ranking {
        fun selectSFWDailyIntegration(count: Int): List<ArtworkInfo>
        fun selectSFWWeeklyIntegration(count: Int): List<ArtworkInfo>
        fun selectSFWRookieIntegration(count: Int): List<ArtworkInfo>
        fun selectSFWOriginalIntegration(count: Int): List<ArtworkInfo>
        fun selectSFWForMaleIntegration(count: Int): List<ArtworkInfo>
        fun selectSFWForFemaleIntegration(count: Int): List<ArtworkInfo>
        fun selectSFWDailyIllustration(count: Int): List<ArtworkInfo>
        fun selectSFWWeeklyIllustration(count: Int): List<ArtworkInfo>
        fun selectSFWRookieIllustration(count: Int): List<ArtworkInfo>
        fun selectSFWOriginalIllustration(count: Int): List<ArtworkInfo>
        fun selectSFWDailyUgoria(count: Int): List<ArtworkInfo>
        fun selectSFWWeeklyUgoria(count: Int): List<ArtworkInfo>
        fun selectNSFWDailyIntegration(count: Int): List<ArtworkInfo>
        fun selectNSFWWeeklyIntegration(count: Int): List<ArtworkInfo>
        fun selectNSFWForMaleIntegration(count: Int): List<ArtworkInfo>
        fun selectNSFWForFemaleIntegration(count: Int): List<ArtworkInfo>
    }

    fun selectionBaseOnArtworkID(block: ArtworkID.() -> Unit)
    fun selectionBaseOnAuthor(block: Author.() -> Unit)
    fun selectionBaseOnTitle(block: Title.() -> Unit)
    fun selectionBaseOnTags(block: Tags.() -> Unit)
    fun selectionBaseOnRanking(block: Ranking.() -> Unit)
}