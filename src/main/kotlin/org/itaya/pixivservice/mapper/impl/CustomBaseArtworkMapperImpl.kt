package org.itaya.pixivservice.mapper.impl

import cn.hutool.core.util.ReUtil
import cn.hutool.core.util.XmlUtil
import cn.hutool.json.JSONUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.itaya.pixivservice.mapper.CustomBaseArtworkMapper
import org.itaya.pixivservice.model.ArtworkInformationModel
import org.itaya.pixivservice.model.ArtworkInformationModelImpl
import org.itaya.pixivservice.utils.HttpRequest
import org.jsoup.Jsoup
import org.seimicrawler.xpath.JXDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.regex.Pattern
import javax.xml.xpath.XPathConstants
import kotlin.collections.ArrayList

@Repository
class CustomBaseArtworkMapperImpl @Autowired constructor(val httpRequest: HttpRequest): CustomBaseArtworkMapper {
    override fun selectById(id: Int): ArtworkInformationModel {
        val document = httpRequest.getString("https://www.pixiv.net/artworks/${id}")
        val jxDocument = JXDocument(Jsoup.parse(document).allElements)
        val title = jxDocument.selNOne("//head/title/text()").toString()
        val name = ReUtil.get("(.*?).-.*?的.. - pixiv", title, 1)
        val author = ReUtil.get(".*?.-(.*?)的.. - pixiv", title, 1)
        val visibility = if (jxDocument.selNOne("//head/meta[@property=\"twitter:title\"]/@content").asString().contains("R-18"))
            ArtworkInformationModel.Visibility.NSFW else ArtworkInformationModel.Visibility.SFW
        val srcFormat = if (jxDocument.selNOne("//head/title/text()").asString().contains("动图"))
            ArtworkInformationModel.Format.GIF else ArtworkInformationModel.Format.IMG
        val views = ReUtil.get("\"viewCount\":(\\d+),", document, 1).toInt()
        val likes = ReUtil.get("\"likeCount\":(\\d+),", document, 1).toInt()
        val bookmarks = ReUtil.get("\"bookmarkCount\":(\\d+),", document, 1).toInt()
        val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ReUtil.get("\"createDate\":\"(.*?)\"},", document, 1))
        val tags = ReUtil.get("\"tags\":\\[([^{}]*?)]", document, 1).split(",").toList()
        return ArtworkInformationModelImpl(
            id,
            name,
            author,
            date,
            tags,
            views,
            likes,
            bookmarks,
            visibility,
            srcFormat
        )
    }

    override fun selectByTitle(name: String): ArtworkInformationModel {
        TODO("Not yet implemented")
    }

    override fun selectByAuthor(name: String): List<ArtworkInformationModel> {
        val document = httpRequest.getString("https://www.pixiv.net/search_user.php?nick=${name}&s_mode=s_usr")
        val data = XmlUtil.getByXPath("//h1/a[@target=\"_blank\"][@class=\"title\"]/@href", document, XPathConstants.STRING).toString()
        val id = ReUtil.get("\\d+", data, 0)
        return selectByAuthor(id)
    }

    override fun selectByAuthor(id: Int): List<ArtworkInformationModel> {
        val json = httpRequest.getString("https://www.pixiv.net/ajax/user/${id}/profile/all?")
        val data = JSONUtil.parseObj(json).getJSONObject("body").getJSONObject("illusts").toString()
        return runBlocking {
            ArrayList<ArtworkInformationModel>().apply {
                ReUtil.findAll(Pattern.compile("(\\d+)"), data, 1).map {
                    launch { add(selectById(it.toInt())) }
                }
            }
        }
    }

    override fun selectByTags(tags: List<String>): List<ArtworkInformationModel> {
        val url = { page: Int ->
            val tagList = URLEncoder.encode(tags.joinToString(" "), StandardCharsets.UTF_8).replace("\\+".toRegex(), "%20")
            if (page == 0) "https://www.pixiv.net/touch/ajax/search/illusts?type=all&word=${tagList}&s_mode=s_tag_full"
            else "https://www.pixiv.net/touch/ajax/search/illusts?p=${page}&type=all&word=${tagList}&s_mode=s_tag_full"
        }
        var page = 0
        return ArrayList<ArtworkInformationModel>().apply {
            runBlocking {
                while (true) {
                    try {
                        val json = httpRequest.getString(url.invoke(page))
                        val dataList = JSONUtil.parseObj(json).getJSONObject("body").getJSONArray("illusts")
                        dataList.forEach {
                            val id = JSONUtil.parseObj(it).getJSONObject("id").toString().toInt()
                            launch { add(selectById(id)) }
                        }
                        ++page
                    }
                    catch (ignored: NullPointerException) { break }
                }
            }
        }
    }
}