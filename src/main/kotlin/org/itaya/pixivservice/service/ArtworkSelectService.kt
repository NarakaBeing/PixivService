package org.itaya.pixivservice.service

import org.itaya.pixivservice.model.ArtworkFilterConfig
import org.itaya.pixivservice.model.ArtworkInformationModel
import java.io.File

interface ArtworkSelectService {
    interface ArtworkID {
        fun select(id: Int): ArtworkInformationModel
        fun selectRecommended(workCount: Int): List<ArtworkInformationModel>
        fun selectRecommendedNSFW(workCount: Int): List<ArtworkInformationModel>
    }
    
    interface Author {
        fun select(name: String): List<ArtworkInformationModel>
        fun select(id: Int): List<ArtworkInformationModel>
        fun selectSubscribed(pageCount: Int): List<ArtworkInformationModel>
        fun selectSubscribedNSFW(pageCount: Int): List<ArtworkInformationModel>
        fun selectRecommended(userCount: Int): Map<Int, List<ArtworkInformationModel>>
    }
    
    interface Title {
        fun select(name: String): ArtworkInformationModel
    }
    
    interface Tags {
        fun select(tags: List<String>): List<ArtworkInformationModel>
    }
    
    interface Ranking {
        fun selectSFWDailyIntegration(count: Int): List<ArtworkInformationModel>
        fun selectSFWWeeklyIntegration(count: Int): List<ArtworkInformationModel>
        fun selectSFWRookieIntegration(count: Int): List<ArtworkInformationModel>
        fun selectSFWOriginalIntegration(count: Int): List<ArtworkInformationModel>
        fun selectSFWForMaleIntegration(count: Int): List<ArtworkInformationModel>
        fun selectSFWForFemaleIntegration(count: Int): List<ArtworkInformationModel>
        fun selectSFWDailyIllustration(count: Int): List<ArtworkInformationModel>
        fun selectSFWWeeklyIllustration(count: Int): List<ArtworkInformationModel>
        fun selectSFWRookieIllustration(count: Int): List<ArtworkInformationModel>
        fun selectSFWOriginalIllustration(count: Int): List<ArtworkInformationModel>
        fun selectSFWDailyUgoria(count: Int): List<ArtworkInformationModel>
        fun selectSFWWeeklyUgoria(count: Int): List<ArtworkInformationModel>
        fun selectNSFWDailyIntegration(count: Int): List<ArtworkInformationModel>
        fun selectNSFWWeeklyIntegration(count: Int): List<ArtworkInformationModel>
        fun selectNSFWForMaleIntegration(count: Int): List<ArtworkInformationModel>
        fun selectNSFWForFemaleIntegration(count: Int): List<ArtworkInformationModel>
    }

    fun selectionBaseOnArtworkID(block: ArtworkID.() -> Unit)
    fun selectionBaseOnAuthor(block: Author.() -> Unit)
    fun selectionBaseOnTitle(block: Title.() -> Unit)
    fun selectionBaseOnTags(block: Tags.() -> Unit)
    fun selectionBaseOnRanking(block: Ranking.() -> Unit)
}