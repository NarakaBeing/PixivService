package org.itaya.pixivservice.mapper

import org.itaya.pixivservice.model.ArtworkInformationModel
import java.io.File

interface ArtworkFileMapper {
    fun downloadArtworkAsFile(artworkInformation: ArtworkInformationModel): List<File>
    fun downloadArtworksAsFiles(artworkInformationList: List<ArtworkInformationModel>): List<List<File>>
}