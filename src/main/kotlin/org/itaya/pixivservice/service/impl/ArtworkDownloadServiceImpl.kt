package org.itaya.pixivservice.service.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.itaya.pixivservice.mapper.ArtworkFileMapper
import org.itaya.pixivservice.model.ArtworkFilter
import org.itaya.pixivservice.model.ArtworkFilterImpl
import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.service.ArtworkDownloadService
import org.itaya.pixivservice.service.ArtworkDownloadService.*
import org.itaya.pixivservice.service.ArtworkDownloadService.FolderOrganizer.*
import org.itaya.pixivservice.service.ArtworkFilterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path

@Service
class ArtworkDownloadServiceImpl @Autowired constructor(
    val artworkFileMapper: ArtworkFileMapper,
    private val artworkFilterServiceImpl: ArtworkFilterService
): ArtworkDownloadService {
    private var folderOrganizer: FolderOrganizer = FolderOrganizerImpl()

    override fun downloadArtworkAsFile(artwork: ArtworkInfo, downloadPath: Path): List<File> {
        return artworkFileMapper.downloadArtworkAsFile(artwork, downloadPath)
    }

    override fun downloadArtworkAsFile(artworkList: List<ArtworkInfo>, config: FolderOrganizer.() -> Unit): List<List<File>> {
        val files: MutableList<List<File>> = ArrayList()
        val mapper = folderOrganizer.apply(config).pathsOf(artworkList)
        runBlocking(Dispatchers.IO) {
            mapper.onEach { (artworkInfo, path) ->
                launch {
                    val fileList = artworkFileMapper.downloadArtworkAsFile(artworkInfo, path)
                    files.add(fileList)
                }
            }
        }
        return files
    }

    override fun organizeFolder(config: FolderOrganizer.() -> Unit) {
        folderOrganizer.apply(config)
    }

    private fun FolderOrganizer.pathsOf(infoList: List<ArtworkInfo>): Map<ArtworkInfo, Path> {
        val result = HashMap<ArtworkInfo, Path>()
        folderHierarchyConfigList.onEach { folderHierarchyConfig ->
            val pendingList = ArrayList<ArtworkInfo>().apply { infoList.map { add(it) } }
            val isReservedSurplus = folderHierarchyConfig.first
            val folderHierarchy = folderHierarchyConfig.second
            folderHierarchy.onEach { folderConfig ->
                val filtrate = artworkFilterServiceImpl.filter(pendingList, folderConfig.filterConfig)
                for (it in filtrate) {
                    result[it] = if (result[it] == null) Path.of(folderConfig.folderName)
                    else Path.of(result[it].toString(), folderConfig.folderName)
                    pendingList.remove(it)
                }
            }
            if (isReservedSurplus) pendingList.onEach { result[it] = Path.of(result[it].toString(), "surplus") }
        }
        return result
    }

    class FolderOrganizerImpl: FolderOrganizer {
        class FolderOrganizationConfigImpl: FolderOrganizationConfig {
            override var folderName: String = "nameless"
            override var filterConfig: ArtworkFilter = ArtworkFilterImpl.nullFilter()
        }
        override val folderHierarchyConfigList = ArrayList<Pair<Boolean, MutableList<FolderOrganizationConfig>>>()
        private var hierarchyCount = 0

        override fun startAt(downloadPath: Path) {
            folderHierarchyConfigList.add(
                Pair(false, ArrayList<FolderOrganizationConfig>().apply {
                    add(FolderOrganizationConfigImpl().apply { folderName = downloadPath.toString() })
                })
            )
        }

        override fun nextHierarchy(isReservedSurplus: Boolean) {
            hierarchyCount++
            folderHierarchyConfigList.add(Pair(isReservedSurplus, ArrayList()))
        }

        override fun addFolder(config: FolderOrganizationConfig.() -> Unit) {
            folderHierarchyConfigList[hierarchyCount].second.add(FolderOrganizationConfigImpl().apply(config))
        }
    }
}