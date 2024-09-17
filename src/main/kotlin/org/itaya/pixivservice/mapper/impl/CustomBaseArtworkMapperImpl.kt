package org.itaya.pixivservice.mapper.impl

import cn.hutool.core.util.ReUtil
import cn.hutool.core.util.XmlUtil
import cn.hutool.json.JSONUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.itaya.pixivservice.mapper.CustomBaseArtworkMapper
import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.model.ArtworkInfoImpl
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
    override fun selectById(id: Int): ArtworkInfo {
        val document = httpRequest.getString("https://www.pixiv.net/artworks/${id}")
        val jxDocument = JXDocument(Jsoup.parse(document).allElements)
        val title = jxDocument.selNOne("//head/title/text()").toString()
        var name = ReUtil.get("(.*?).-.*?的.. - pixiv", title, 1)
        var author = ReUtil.get(".*?.-(.*?)的.. - pixiv", title, 1)
        val visibility = if (jxDocument.selNOne("//head/meta[@property=\"twitter:title\"]/@content").asString().contains("R-18"))
            ArtworkInfo.Visibility.NSFW else ArtworkInfo.Visibility.SFW
        val srcFormat = if (jxDocument.selNOne("//head/title/text()").asString().contains("动图"))
            ArtworkInfo.Format.GIF else ArtworkInfo.Format.IMG
        name = name ?: "failure"
        author = author ?: "failure"
        val views = ReUtil.get("\"viewCount\":(\\d+),", document, 1).toInt()
        val likes = ReUtil.get("\"likeCount\":(\\d+),", document, 1).toInt()
        val bookmarks = ReUtil.get("\"bookmarkCount\":(\\d+),", document, 1).toInt()
        val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ReUtil.get("\"createDate\":\"(.*?)\"},", document, 1))
        val tags = ReUtil.get("\"tags\":\\[([^{}]*?)]", document, 1).split(",").toList()
        return ArtworkInfoImpl(
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

    override fun selectById(ids: List<Int>): List<ArtworkInfo> {
        val informationList = ArrayList<ArtworkInfo>();
        runBlocking(Dispatchers.IO) {
            ids.onEach {
                launch { informationList.add(selectById(it)) }
            }
        }
        return informationList
    }

    override fun selectByTitle(name: String, count: Int): ArtworkInfo {
        TODO("Not yet implemented")
    }


    override fun selectByAuthor(name: String, count: Int): List<ArtworkInfo> {
        val document = httpRequest.getString("https://www.pixiv.net/search_user.php?nick=${name}&s_mode=s_usr")
        val data = XmlUtil.getByXPath("//h1/a[@target=\"_blank\"][@class=\"title\"]/@href", document, XPathConstants.STRING).toString()
        val id = ReUtil.get("\\d+", data, 0)
        return selectByAuthor(id, count)
    }

    override fun selectByAuthor(id: Int, count: Int): List<ArtworkInfo> {
        val json = httpRequest.getString("https://www.pixiv.net/ajax/user/${id}/profile/all?")
        val data = JSONUtil.parseObj(json).getJSONObject("body").getJSONObject("illusts").toString()
        val ids = ReUtil.findAll(Pattern.compile("(\\d+)"), data, 1).map { it.toInt() }.subList(0, count)
        return selectById(ids)
    }

    override fun selectByTags(tags: List<String>, count: Int): List<ArtworkInfo> {
        fun getUrl(pageCount: Int): String {
            val tagList = URLEncoder.encode(tags.joinToString(" "), StandardCharsets.UTF_8).replace("\\+".toRegex(), "%20")
            return if (pageCount == 0) "https://www.pixiv.net/touch/ajax/search/illusts?type=all&word=${tagList}&s_mode=s_tag_full"
            else "https://www.pixiv.net/touch/ajax/search/illusts?p=${pageCount}&type=all&word=${tagList}&s_mode=s_tag_full"
        }
        var page = 0
        val idList = ArrayList<Int>()
        runBlocking {
            while (idList.size < count) {
                try {
                    launch {
                        val json = httpRequest.getString(getUrl(page++))
                        val dataList = JSONUtil.parseObj(json).getJSONObject("body").getJSONArray("illusts")
                        dataList.map { idList.add(JSONUtil.parseObj(it).getJSONObject("id").toString().toInt()) }
                    }
                }
                catch (ignored: NullPointerException) { break }
            }
        }
        return selectById(idList)
    }
}