package org.itaya.pixivservice.mapper.impl

import cn.hutool.core.img.gif.AnimatedGifEncoder
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.ZipUtil
import cn.hutool.json.JSONUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.itaya.pixivservice.mapper.ArtworkFileMapper
import org.itaya.pixivservice.model.ArtworkInformationModel
import org.itaya.pixivservice.utils.HttpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.io.*
import java.nio.file.Path
import javax.imageio.ImageIO

@Repository
class ArtworkFileMapperImpl @Autowired constructor(val httpRequest: HttpRequest): ArtworkFileMapper {
    @Value("\${file.download_file_dir}")
    open lateinit var downloadFileDest: String

    override fun downloadArtworkAsFile(artworkInformation: ArtworkInformationModel): List<File> {
        return when (artworkInformation.format) {
            ArtworkInformationModel.Format.IMG -> downloadIMGs(artworkInformation)
            ArtworkInformationModel.Format.GIF -> downloadGIF(artworkInformation)
            else -> throw Exception("")
        }
    }

    override fun downloadArtworksAsFiles(artworkInformationList: List<ArtworkInformationModel>): List<List<File>> {
        return ArrayList<List<File>>().apply {
            artworkInformationList.onEach { add(downloadArtworkAsFile(it)) }
        }
    }

    private fun downloadIMGs(artworkInformationModel: ArtworkInformationModel): List<File> {
        val srcUrls =  ArrayList<String>().also { srcUrls ->
            val originSrcUrl = "https://www.pixiv.net/ajax/illust/${artworkInformationModel.id}/pages?lang=zh"
            val json = JSONUtil.parseObj(httpRequest.getString(originSrcUrl))
            json.getJSONArray("body").forEach {
                val url = JSONUtil.parseObj(it).getJSONObject("urls")["original"].toString()
                srcUrls.add(url)
            }
        }
        val imgFormat = srcUrls.stream().map { it.substring(it.length - 3) }.toList()
        return ArrayList<File>().also { files ->
            runBlocking(Dispatchers.IO) {
                repeat(imgFormat.size) { i ->
                    launch {
                        val fileDest = Path.of(downloadFileDest, artworkInformationModel.author, "${artworkInformationModel.title}_p${i}.${imgFormat[i]}").toFile()
                        httpRequest.downloadFile((srcUrls[i]), fileDest)
                        files.add(fileDest)
                    }
                }
            }
        }
    }

    private fun downloadGIF(artworkInformationModel: ArtworkInformationModel): List<File> {
        val tempDir = "temp"
        val json = httpRequest.getString("https://www.pixiv.net/ajax/illust/${artworkInformationModel.id}/ugoira_meta?lang=zh")
        val delayMs = JSONUtil.parseObj(json).getJSONObject("body").getJSONArray("frames").map {
            JSONUtil.parseObj(it).getDouble("delay") }
            .toList().run { (sum() / size).toInt() }
        val frameList = runBlocking(Dispatchers.IO) {
            val srcUrl = JSONUtil.parseObj(json).getJSONObject("body")["originalSrc"].toString()
            val zipFileDest = Path.of(tempDir, "${artworkInformationModel.id}.zip").toFile()
            val unzipFileDest = Path.of(tempDir,"${artworkInformationModel.id}").toFile()
            httpRequest.downloadFile(srcUrl, zipFileDest)
            ZipUtil.unzip(zipFileDest, unzipFileDest).listFiles()!!.map { ImageIO.read(it) }
        }
        FileUtil.mkdir(Path.of(downloadFileDest, artworkInformationModel.author))
        val gifFileDest = Path.of(downloadFileDest, artworkInformationModel.author, "${artworkInformationModel.id}.gif").toFile()
        AnimatedGifEncoder().apply {
            start(FileOutputStream(gifFileDest))
            setDelay(delayMs)
            frameList.forEach { addFrame(it) }
            finish()
        }
        FileUtil.del(tempDir)
        return listOf(gifFileDest)
    }
}