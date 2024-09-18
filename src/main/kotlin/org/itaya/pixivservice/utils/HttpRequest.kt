package org.itaya.pixivservice.utils

import cn.hutool.core.io.FileUtil
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.Header
import org.apache.hc.core5.http.HttpEntity
import org.apache.hc.core5.http.HttpHost
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.message.BasicHeader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.Proxy
import java.net.ProxySelector
import java.net.URI
import java.util.regex.Pattern

@Component
class HttpRequest {
    companion object {
        var cookie: String = "";
    }

    private val userAgent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54"

    private val referer: String = "https://www.pixiv.net"


    private val systemProxy: HttpHost? = run {
        ProxySelector.getDefault().select(URI("https://www.pixiv.com")).onEach<Proxy?, MutableList<Proxy?>> { proxy ->
            val m = Pattern.compile("(.*?)/.*?:(\\d+)").matcher(proxy?.address().toString())
            if (proxy?.type() == Proxy.Type.HTTP && m.find()) return@run HttpHost(m.group(1), m.group(2).toInt())
        }
        null
    }

    private val httpClient: CloseableHttpClient = HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom().build())
        .setDefaultHeaders(
            listOf<Header>(
                BasicHeader("cookie", cookie),
                BasicHeader("User-Agent", userAgent),
                BasicHeader("Referer", referer)
            )
        )
        .apply<HttpClientBuilder> { setProxy(systemProxy) }
        .build()

    private fun download(httpUrl: String): HttpEntity {
        return httpClient.execute(HttpGet(httpUrl)).entity
    }

    fun getString(httpUrl: String): String {
        return EntityUtils.toString(download(httpUrl) , "utf-8")
    }

    fun downloadFile(httpUrl: String, fileDest: java.io.File) {
        val bytes = EntityUtils.toByteArray(download(httpUrl))
        FileUtil.writeBytes(bytes, fileDest)
    }
}

