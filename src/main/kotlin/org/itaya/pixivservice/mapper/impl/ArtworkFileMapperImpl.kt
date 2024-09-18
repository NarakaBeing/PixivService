package org.itaya.pixivservice.mapper.impl

import cn.hutool.core.img.gif.AnimatedGifEncoder
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.ZipUtil
import cn.hutool.json.JSONUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.itaya.pixivservice.mapper.ArtworkFileMapper
import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.utils.HttpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.io.*
import java.nio.file.Path
import javax.imageio.ImageIO

@Repository
class ArtworkFileMapperImpl @Autowired constructor(val httpRequest: HttpRequest): ArtworkFileMapper {
    override fun downloadArtworkAsFile(artworkInfo: ArtworkInfo, downloadPath: Path): List<File> {
        return when (artworkInfo.format) {
            ArtworkInfo.Format.IMG -> downloadIMGs(artworkInfo, downloadPath)
            ArtworkInfo.Format.GIF -> downloadGIF(artworkInfo, downloadPath)
        }
    }


    private fun downloadIMGs(artworkInfo: ArtworkInfo, downloadPath: Path): List<File> {
        val srcUrls =  ArrayList<String>().also { srcUrls ->
            val originSrcUrl = "https://www.pixiv.net/ajax/illust/${artworkInfo.id}/pages?lang=zh"
            val json = JSONUtil.parseObj(httpRequest.getString(originSrcUrl))
            json.getJSONArray("body").forEach {
                val url = JSONUtil.parseObj(it).getJSONObject("urls")["original"].toString()
                srcUrls.add(url)
            }
        }
        val imgFormat = srcUrls.stream().map { it.substring(it.length - 3) }.toList()
        val files = ArrayList<File>()

        FileUtil.mkdir(downloadPath)
        runBlocking(Dispatchers.IO) {
            repeat(imgFormat.size) { i ->
                val name = "${artworkInfo.title}_p${i}.${imgFormat[i]}"
                launch {
                    val fileDest = Path.of(downloadPath.toString(), name).toFile()
                    httpRequest.downloadFile((srcUrls[i]), fileDest)
                    files.add(fileDest)
                }
            }
        }
        return files
    }

    private fun downloadGIF(artworkInfo: ArtworkInfo, downloadPath: Path): List<File> {
        val tempDir = "temp"
        val json = httpRequest.getString("https://www.pixiv.net/ajax/illust/${artworkInfo.id}/ugoira_meta?lang=zh")
        val delayMs = JSONUtil.parseObj(json).getJSONObject("body").getJSONArray("frames").map {
            JSONUtil.parseObj(it).getDouble("delay") }
            .toList().run { (sum() / size).toInt() }

        val srcUrl = JSONUtil.parseObj(json).getJSONObject("body")["originalSrc"].toString()
        val zipFileDest = Path.of(tempDir, "${artworkInfo.id}.zip").toFile()
        val unzipFileDest = Path.of(tempDir, "${artworkInfo.id}").toFile()
        httpRequest.downloadFile(srcUrl, zipFileDest)
        val frameList = ZipUtil.unzip(zipFileDest, unzipFileDest).listFiles()!!.map { ImageIO.read(it) }

        val name = "${artworkInfo.title}.gif"
        val gifFileDest = Path.of(downloadPath.toString(), name).toFile()

        FileUtil.mkdir(downloadPath)
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