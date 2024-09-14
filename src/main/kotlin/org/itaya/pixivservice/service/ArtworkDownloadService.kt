package org.itaya.pixivservice.service

import org.itaya.pixivservice.model.ArtworkInformationModel
import java.io.File

interface ArtworkDownloadService {
    fun downloadArtworkAsFile(artworkInformationModel: ArtworkInformationModel): List<File>
    fun downloadArtworkAsFile(artworkInformationModelList: List<ArtworkInformationModel>): List<List<File>>
}