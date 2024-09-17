package org.itaya.pixivservice.model

import java.util.*

interface ArtworkInfo {
    var id: Int
    var title: String
    var author: String
    var date: Date
    var tag: List<String>
    var views: Int
    var likes: Int
    var bookmarks: Int
    var visibility: Visibility
    var format: Format
    enum class Visibility { NSFW, SFW }
    enum class Format { IMG, GIF }
}