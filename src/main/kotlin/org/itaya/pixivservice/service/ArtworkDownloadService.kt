package org.itaya.pixivservice.service

import org.itaya.pixivservice.model.ArtworkFilter
import org.itaya.pixivservice.model.ArtworkInfo
import java.io.File
import java.nio.file.Path

interface ArtworkDownloadService {
    fun downloadArtworkAsFile(artwork: ArtworkInfo, downloadPath: Path): List<File>
    fun downloadArtworkAsFile(artworkList: List<ArtworkInfo>, config: FolderOrganizer.() -> Unit): List<List<File>>
    fun organizeFolder(config: FolderOrganizer.() -> Unit);

    interface FolderOrganizer {
        interface FolderOrganizationConfig {
            var folderName: String
            var filterConfig: ArtworkFilter
        }
        val folderHierarchyConfigList: ArrayList<Pair<Boolean, MutableList<FolderOrganizationConfig>>>
        fun startAt(downloadPath: Path)
        fun nextHierarchy(isReservedSurplus: Boolean);
        fun addFolder(config: FolderOrganizationConfig.() -> Unit)
    }
}