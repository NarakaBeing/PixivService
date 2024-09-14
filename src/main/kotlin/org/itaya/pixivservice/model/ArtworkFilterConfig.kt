package org.itaya.pixivservice.model;


import java.util.Date;

interface ArtworkFilterConfig {
    var id: Int?;
    var title: String?;
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
    var visibility: ArtworkInformationModel.Visibility?;
    var format: ArtworkInformationModel.Format?
}
