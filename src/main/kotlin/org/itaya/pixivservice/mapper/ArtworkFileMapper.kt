package org.itaya.pixivservice.mapper

import org.itaya.pixivservice.model.ArtworkInfo
import java.io.File
import java.nio.file.Path

interface ArtworkFileMapper {
    fun downloadArtworkAsFile(artworkInfo: ArtworkInfo, downloadPath: Path): List<File>
}