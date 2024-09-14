package org.itaya.pixivservice.service

import org.itaya.pixivservice.model.ArtworkFilterConfig
import org.itaya.pixivservice.model.ArtworkInformationModel

interface ArtworkFilterService {
    fun createFilterConfig(config: (ArtworkFilterConfig) -> Unit): ArtworkFilterConfig
    fun filter(artworkInformationModelList: List<ArtworkInformationModel>, config: ArtworkFilterConfig): List<ArtworkInformationModel>
    fun filter(artworkInformationModelList: List<ArtworkInformationModel>, config: (ArtworkFilterConfig) -> Unit): List<ArtworkInformationModel>
}