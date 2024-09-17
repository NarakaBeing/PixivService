package org.itaya.pixivservice.service

import org.itaya.pixivservice.model.ArtworkFilter
import org.itaya.pixivservice.model.ArtworkInfo
import java.io.File
import java.nio.file.Path

interface ArtworkDownloadService {
    fun downloadArtworkAsFile(artwork: ArtworkInfo, downloadPath: Path): List<File>
    fun downloadArtworkAsFile(artworkList: List<ArtworkInfo>, config: FolderGradingConfigurer.() -> Unit): List<List<File>>
    fun folderGradingConfig(config: FolderGradingConfigurer.() -> Unit);

    interface FolderGradingConfigurer {
        interface FolderGradingConfig {
            var folderName: String
            var filterConfig: ArtworkFilter
        }
        val folderLevelConfigList: ArrayList<Pair<Boolean, MutableList<FolderGradingConfig>>>
        fun startAt(downloadPath: Path)
        fun nextLevel(isReservedSurplus: Boolean);
        fun addFolder(config: (FolderGradingConfig) -> Unit)
    }
}