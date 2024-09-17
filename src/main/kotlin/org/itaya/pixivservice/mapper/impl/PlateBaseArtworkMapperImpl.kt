package org.itaya.pixivservice.mapper.impl

import cn.hutool.json.JSONUtil
import kotlinx.coroutines.*
import org.itaya.pixivservice.mapper.CustomBaseArtworkMapper
import org.itaya.pixivservice.mapper.PlateBaseArtworkMapper
import org.itaya.pixivservice.mapper.impl.PlateBaseArtworkMapperImpl.Companion.AgeLimit.*
import org.itaya.pixivservice.mapper.impl.PlateBaseArtworkMapperImpl.Companion.RankingContext.*
import org.itaya.pixivservice.mapper.impl.PlateBaseArtworkMapperImpl.Companion.RankingMode.*
import org.itaya.pixivservice.model.ArtworkInfo
import org.itaya.pixivservice.utils.HttpRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Repository
class PlateBaseArtworkMapperImpl @Autowired constructor(
    val httpRequest: HttpRequest,
    val customBaseArtworkMapperImpl: CustomBaseArtworkMapper
): PlateBaseArtworkMapper {
    companion object {
        enum class AgeLimit(val ageLimit: String) {
            SFW("all"), NSFW("r18")
        }
        enum class RankingMode {
            DAILY, WEEKLY, MONTHLY, ROOKIE, ORIGINAL, DAILY_AI, MALE, FEMALE
        }
        enum class RankingContext {
            INTEGRATION, ILLUST, UGOIRA, MANGO
        }
    }

    private fun selectBySubscriptAuthors(count: Int, ageLimit :AgeLimit): List<ArtworkInfo> {
        var page = 1
        val idList = ArrayList<Int>()
        fun getIDs(pageCount: Int): Boolean {
            val url = "https://www.pixiv.net/ajax/follow_latest/illust?p=$pageCount&mode=${ageLimit.ageLimit}&lang=zh"
            val json = httpRequest.getString(url)
            if (JSONUtil.parseObj(json).getBool("error")) return false
            val result = JSONUtil.parseObj(json)
                .getJSONObject("body")
                .getJSONObject("page")
                .getJSONArray("ids")
                .map { it.toString().toInt() }
            idList.addAll(result);
            return true
        }
        while (getIDs(page) && idList.size < count) page++;
        return customBaseArtworkMapperImpl.selectById(idList.subList(0, count));
    }

    override fun selectBySubscriptAuthors(count: Int): List<ArtworkInfo> {
       return selectBySubscriptAuthors(count, SFW)
    }

    override fun selectNSFWBySubscriptAuthors(count: Int): List<ArtworkInfo> {
        return selectBySubscriptAuthors(count, NSFW)
    }

    private fun selectByRecommendedWorks(workCount: Int, ageLimit: AgeLimit): List<ArtworkInfo> {
        val url = "https://www.pixiv.net/ajax/discovery/artworks?mode=${ageLimit.ageLimit}&limit=$workCount&lang=zh"
        val json = httpRequest.getString(url)
        val idList = JSONUtil.parseObj(json)
            .getJSONObject("body")
            .getJSONObject("thumbnails")
            .getJSONArray("illust")
            .map {
                val id = JSONUtil.parseObj(it).getStr("id")
                id.toInt()
            }
        return customBaseArtworkMapperImpl.selectById(idList)
    }

    override fun selectByRecommendedWorks(count: Int): List<ArtworkInfo> {
        return selectByRecommendedWorks(count, SFW)
    }

    override fun selectNSFWByRecommendedWorks(count: Int): List<ArtworkInfo> {
        return selectByRecommendedWorks(count, NSFW)
    }

    override fun selectByRecommendedAuthors(userCount: Int): Map<Int, List<ArtworkInfo>> {
        val url = "https://www.pixiv.net/ajax/discovery/users?limit=$userCount&lang=zh"
        val json = httpRequest.getString(url)
        val mapper = HashMap<Int, List<ArtworkInfo>>()
        JSONUtil.parseObj(json)
            .getJSONObject("body")
            .getJSONObject("thumbnails")
            .getJSONArray("illust")
            .onEach {
                val jsonObj = JSONUtil.parseObj(it)
                val workIds = jsonObj.getJSONArray("id").map { e -> e.toString().toInt() }
                val workInformation = customBaseArtworkMapperImpl.selectById(workIds)
                val authorId = jsonObj.getStr("userId").toInt()
                mapper[authorId] = workInformation
            }
        return mapper
    }

    override fun selectSFWByDailyIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, DAILY, INTEGRATION);
    }

    override fun selectSFWByWeeklyIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, WEEKLY, INTEGRATION)
    }

    override fun selectSFWByRookieIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, ROOKIE, INTEGRATION)
    }

    override fun selectSFWByOriginalIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, ORIGINAL, INTEGRATION)
    }

    override fun selectSFWByForMaleIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, MALE, INTEGRATION)
    }

    override fun selectSFWByForFemaleIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, FEMALE, INTEGRATION)
    }

    override fun selectSFWByDailyIllustrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, DAILY_AI, ILLUST)
    }

    override fun selectSFWByWeeklyIllustrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, WEEKLY, ILLUST)
    }

    override fun selectSFWByRookieIllustrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, ROOKIE, ILLUST)
    }

    override fun selectSFWByOriginalIllustrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, ORIGINAL, ILLUST)
    }

    override fun selectSFWByDailyUgoriaRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, DAILY_AI, UGOIRA)
    }

    override fun selectSFWByWeeklyUgoriaRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, SFW, WEEKLY, UGOIRA)
    }

    override fun selectNSFWByDailyIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, NSFW, DAILY, INTEGRATION)
    }

    override fun selectNSFWByWeeklyIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, NSFW, WEEKLY, INTEGRATION)
    }

    override fun selectNSFWByForMaleIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, NSFW, MALE, INTEGRATION)
    }

    override fun selectNSFWByForFemaleIntegrationRanking(count: Int): List<ArtworkInfo> {
        return selectByRanking(count, NSFW, FEMALE, INTEGRATION)
    }

    private fun selectByRanking(count: Int, ageLimit: AgeLimit, mode: RankingMode, context: RankingContext): List<ArtworkInfo> {
        val contextStr = if (context == RankingContext.INTEGRATION) ""
        else "&content=${context.toString().lowercase(Locale.getDefault())}"
        val sfwModeStr = mode.toString().lowercase(Locale.getDefault())
        val nsfwModeStr = when(mode) {
            DAILY, WEEKLY, MALE, FEMALE -> "${sfwModeStr}_r18"
            DAILY_AI -> "daily_r18_ai${contextStr}"
            else -> ""
        }
        val urlTemplate = when (ageLimit) {
            SFW -> "https://www.pixiv.net/ranking.php?mode=${sfwModeStr}&${contextStr}"
            NSFW -> "https://www.pixiv.net/ranking.php?mode=${nsfwModeStr}&${contextStr}"
        }
        fun getUrl(pageCount: Int): String {
            return "$urlTemplate&p=$pageCount"
        }
        var page = 1
        val idList = ArrayList<Int>()
        runBlocking(Dispatchers.IO) {
            val idPattern =  Pattern.compile("\"data-type=\".*?\"data-id=\"(.*?)\"")
            fun getIDs(url: String): List<Int> {
                val data = httpRequest.getString(url)
                return ArrayList<Int>().apply{
                    idPattern.matcher(data).let {
                        while (it.find()) { add(it.group(1).toInt()) }
                    }
                }
            }
            while (idList.size < count) launch { idList.addAll(getIDs(getUrl(page++))) }
        }
        return customBaseArtworkMapperImpl.selectById(idList)
    }
}