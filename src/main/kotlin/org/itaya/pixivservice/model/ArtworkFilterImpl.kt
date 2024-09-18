package org.itaya.pixivservice.model

import java.util.*


class ArtworkFilterImpl(
    override var author: String?,
    override var dateBefore: Date?,
    override var dateAfter: Date?,
    override var includedTags: List<String>?,
    override var excludedTags: List<String>?,
    override var viewsGreaterThan: Int?,
    override var viewsSmallerThan: Int?,
    override var likesGreaterThan: Int?,
    override var bookmarksGreaterThan: Int?,
    override var likesSmallerThan: Int?,
    override var bookmarksSmallerThan: Int?,
    override var visibility: ArtworkInfo.Visibility?,
    override var format: ArtworkInfo.Format?
) : ArtworkFilter {
    companion object {
        fun nullFilter(): ArtworkFilter {
            return ArtworkFilterImpl(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
        }
    }

    override fun toString(): String {
        return """
            author:$author
            dateBefore:${dateBefore.toString()}
            dateAfter:${dateAfter.toString()}
            includedTags:${includedTags.toString()}
            excludedTags:${excludedTags.toString()}
            viewsGreaterThan:$viewsGreaterThan
            viewsSmallerThan:$viewsSmallerThan
            likesGreaterThan:$likesGreaterThan
            bookmarksGreaterThan:$bookmarksGreaterThan
            likesSmallerThan:$likesSmallerThan
            bookmarksSmallerThan:$bookmarksSmallerThan
            visibility:$visibility
            format:$format
            -------------
        """.trimIndent()
    }
}
