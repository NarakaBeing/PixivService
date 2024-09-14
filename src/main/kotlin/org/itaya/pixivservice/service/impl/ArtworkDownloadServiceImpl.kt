package org.itaya.pixivservice.service.impl

import org.itaya.pixivservice.mapper.ArtworkFileMapper
import org.itaya.pixivservice.model.ArtworkInformationModel
import org.itaya.pixivservice.service.ArtworkDownloadService
import org.itaya.pixivservice.service.ArtworkSelectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class ArtworkDownloadServiceImpl @Autowired constructor(val artworkFileMapper: ArtworkFileMapper): ArtworkDownloadService {
    override fun downloadArtworkAsFile(artworkInformationModel: ArtworkInformationModel): List<File> {
        return artworkFileMapper.downloadArtworkAsFile(artworkInformationModel)
    }

    override fun downloadArtworkAsFile(artworkInformationModelList: List<ArtworkInformationModel>): List<List<File>> {
        return artworkFileMapper.downloadArtworksAsFiles(artworkInformationModelList)
    }
}