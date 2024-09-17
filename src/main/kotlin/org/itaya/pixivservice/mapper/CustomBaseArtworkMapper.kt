package org.itaya.pixivservice.mapper

import org.itaya.pixivservice.model.ArtworkInfo

interface CustomBaseArtworkMapper {
    fun selectById(id: Int): ArtworkInfo
    fun selectById(ids: List<Int>): List<ArtworkInfo>
    fun selectByTitle(name: String, count: Int): ArtworkInfo
    fun selectByAuthor(name: String, count: Int): List<ArtworkInfo>
    fun selectByAuthor(id: Int, count: Int): List<ArtworkInfo>
    fun selectByTags(tags: List<String>, count: Int): List<ArtworkInfo>
}