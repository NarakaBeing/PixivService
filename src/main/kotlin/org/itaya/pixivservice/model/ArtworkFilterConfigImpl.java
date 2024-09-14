package org.itaya.pixivservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtworkFilterConfigImpl implements ArtworkFilterConfig {
    public int id;
    public String title;
    public String author;
    public Date dateBefore;
    public Date dateAfter;
    public List<String> includedTags;
    public List<String> excludedTags;
    public int viewsGreaterThan;
    public int viewsSmallerThan;
    public int likesGreaterThan;
    public int bookmarksGreaterThan;
    public int likesSmallerThan;
    public int bookmarksSmallerThan;
    public ArtworkInformationModel.Visibility visibility;
    public ArtworkInformationModel.Format format;

    @Override
    public void setId(Integer integer) {
        id = integer;
    }

    @Override
    public void setViewsGreaterThan(Integer integer) {
        viewsGreaterThan = integer;
    }

    @Override
    public void setViewsSmallerThan(Integer integer) {
        viewsSmallerThan = integer;
    }

    @Override
    public void setLikesGreaterThan(Integer integer) {
        likesSmallerThan = integer;
    }

    @Override
    public void setBookmarksGreaterThan(Integer integer) {
        bookmarksSmallerThan = integer;
    }

    @Override
    public void setLikesSmallerThan(Integer integer){
        likesSmallerThan = integer;
    }

    @Override
    public void setBookmarksSmallerThan(Integer integer) {
        bookmarksSmallerThan = integer;
    }
}
