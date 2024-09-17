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
import org.itaya.pixivservice.service.ArtworkFilterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path

@Service
class ArtworkDownloadServiceImpl @Autowired constructor(
    val artworkFileMapper: ArtworkFileMapper,
    private val artworkFilterServiceImpl: ArtworkFilterService
): ArtworkDownloadService {
    private var folderConfig: FolderGradingConfigurer = FolderGradingConfigurerImpl()

    override fun downloadArtworkAsFile(artwork: ArtworkInfo, downloadPath: Path): List<File> {
        return artworkFileMapper.downloadArtworkAsFile(artwork, downloadPath)
    }

    override fun downloadArtworkAsFile(artworkList: List<ArtworkInfo>, config: FolderGradingConfigurer.() -> Unit): List<List<File>> {
        val files: MutableList<List<File>> = ArrayList()
        val mapper = folderConfig.apply(config).getFullPaths(artworkList)
        runBlocking(Dispatchers.IO) {
            mapper.onEach {
                launch {
                    val fileList = artworkFileMapper.downloadArtworkAsFile(it.key, it.value)
                    files.add(fileList)
                }
            }
        }
        return files
    }

    override fun folderGradingConfig(config: FolderGradingConfigurer.() -> Unit) {
        folderConfig.apply(config)
    }

    private fun FolderGradingConfigurer.getFullPaths(infoList: List<ArtworkInfo>): Map<ArtworkInfo, Path> {
        val result = HashMap<ArtworkInfo, Path>()
        folderLevelConfigList.onEach {folderLevelConfig ->
            val pendingList = ArrayList<ArtworkInfo>().apply { infoList.map { add(it) } }
            val isReservedSurplus = folderLevelConfig.first
            val folderLevel = folderLevelConfig.second
            folderLevel.onEach { folderConfig ->
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

    class FolderGradingConfigurerImpl: FolderGradingConfigurer {
        class FolderGradingConfigImpl: FolderGradingConfigurer.FolderGradingConfig {
            override var folderName: String = "nameless"
            override var filterConfig: ArtworkFilter = ArtworkFilterImpl.nullFilter()
        }
        override val folderLevelConfigList = ArrayList<Pair<Boolean, MutableList<FolderGradingConfigurer.FolderGradingConfig>>>()
        private var levelCount = 0

        override fun startAt(downloadPath: Path) {
            folderLevelConfigList.add(
                Pair(false, ArrayList<FolderGradingConfigurer.FolderGradingConfig>().apply {
                    add(FolderGradingConfigImpl().apply { folderName = downloadPath.toString() })
                })
            )
        }

        override fun nextLevel(isReservedSurplus: Boolean) {
            levelCount++
            folderLevelConfigList.add(Pair(isReservedSurplus, ArrayList()))
        }

        override fun addFolder(config: (FolderGradingConfigurer.FolderGradingConfig) -> Unit) {
            folderLevelConfigList[levelCount].second.add(FolderGradingConfigImpl().apply(config))
        }
    }
}