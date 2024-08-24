package com.stocknews.stock_news.mintnewsentity;

public class MintNews {

    private String heading;
    private String content;
    private String date;
    private String source;

    public MintNews(String heading, String content, String date, String source) {
        this.heading = heading;
        this.content = content;
        this.date = date;
        this.source = source;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
