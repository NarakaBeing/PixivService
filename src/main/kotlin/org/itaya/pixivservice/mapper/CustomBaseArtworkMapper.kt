package org.itaya.pixivservice.mapper

import org.itaya.pixivservice.model.ArtworkInformationModel

interface CustomBaseArtworkMapper {
    fun selectById(id: Int): ArtworkInformationModel
    fun selectByTitle(name: String): ArtworkInformationModel
    fun selectByAuthor(name: String): List<ArtworkInformationModel>
    fun selectByAuthor(id: Int): List<ArtworkInformationModel>
    fun selectByTags(tags: List<String>): List<ArtworkInformationModel>
}