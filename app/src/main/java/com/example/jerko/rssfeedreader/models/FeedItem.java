package com.example.jerko.rssfeedreader.models;

/**
 * Created by Jerko on 13.3.2017..
 */

public class FeedItem {

    String title;
    String link;
    String description;
    String imageSrc;

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }


}
