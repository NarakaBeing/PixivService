package org.itaya.pixivservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
public class ArtworkInformationModelImpl implements ArtworkInformationModel {
    public int id;
    public String title;
    public String author;
    public Date date;
    public List<String> tag;

    public int views;
    public int likes;
    public int bookmarks;

    public Visibility visibility;
    public Format format;
}
