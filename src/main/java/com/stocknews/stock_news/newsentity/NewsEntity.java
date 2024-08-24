package com.stocknews.stock_news.newsentity;

public class NewsEntity {

    private String heading;
    private String content;
    private String thumbnail;

    public NewsEntity(String heading, String content, String thumbnail) {
        this.heading = heading;
        this.content = content;
        this.thumbnail = thumbnail;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
