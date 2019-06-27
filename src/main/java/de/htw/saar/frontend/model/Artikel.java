package de.htw.saar.frontend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Artikel {

    private String id;
    private double score;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Europe/Berlin")
    private Date created;
    private String caption;
    private String content;
    private String url;
    private Boolean isFromFeed;
    private String feedUrl;

    public Artikel()
    {}

    public String getId() { return  this.id; }

    public double getScore() { return this.score; }

    public Date getCreated(){ return this.created; }

    public String getCreatedAsString(){
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
        return dateFormat.format(this.created);
    }
    public String getCreatedDateAsString(){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        return dateFormat.format(this.created);
    }
    public String getCreatedTimeAsString(){
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        return dateFormat.format(this.created);
    }
    public int getCreatedYearAsInt(){
        String pattern = "yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return Integer.parseInt(simpleDateFormat.format(this.created));
    }
    public int getCreatedMonthAsInt(){
        String pattern = "MM";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return Integer.parseInt(simpleDateFormat.format(this.created));
    }

    public String getCaption(){ return this.caption; }

    public String getContent(){ return this.content; }

    public String getUrl(){ return this.url; }

    public Boolean getIsFromFeed(){ return this.isFromFeed; }

    public String getFeedUrl() { return this.feedUrl; }

    public void setId(String id) {
        this.id = id;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFromFeed(Boolean fromFeed) {
        isFromFeed = fromFeed;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

}
