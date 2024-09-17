package org.itaya.pixivservice.model;


import java.util.Date;

interface ArtworkFilter {
    var author: String?;
    var dateBefore: Date?;
    var dateAfter: Date?;
    var includedTags: List<String>?;
    var excludedTags: List<String>?;
    var viewsGreaterThan: Int?;
    var viewsSmallerThan: Int?;
    var likesGreaterThan: Int?;
    var bookmarksGreaterThan: Int?;
    var likesSmallerThan: Int?;
    var bookmarksSmallerThan: Int?;
    var visibility: ArtworkInfo.Visibility?;
    var format: ArtworkInfo.Format?
}
