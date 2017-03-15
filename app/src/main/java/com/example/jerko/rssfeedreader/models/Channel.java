package com.example.jerko.rssfeedreader.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerko on 13.3.2017..
 */

public class Channel {
    private String title;
    private String link;
    private List<FeedItem> feedItems = new ArrayList<>();

    public String getTitle(){
        return title;
    }
    public String getLink(){
        return link;
    }
    public List<FeedItem> getFeedItems(){
        return feedItems;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setFeedItems(List<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }
}
