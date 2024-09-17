package org.itaya.pixivservice.model

import java.util.*

class ArtworkInfoImpl(
    override var id: Int,
    override var title: String,
    override var author: String,
    override var date: Date,
    override var tag: List<String>,
    override var views: Int,
    override var likes: Int,
    override var bookmarks: Int,
    override var visibility: ArtworkInfo.Visibility,
    override var format: ArtworkInfo.Format
) : ArtworkInfo {
    override fun toString(): String {
        return """
            id:$id
            title:$title
            author:$author
            date:$date
            tag:$tag
            views:$views
            likes:$likes
            bookmarks:$bookmarks
            visibility:$visibility
            format:$format
        """.trimIndent()
    }
}
