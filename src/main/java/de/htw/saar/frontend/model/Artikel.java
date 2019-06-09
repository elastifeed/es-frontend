package de.htw.saar.frontend.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Artikel {

    private String id;
    private double score;
    private Date created;
    private String caption;
    private String content;
    private String url;
    private Boolean isFromFeed;
    private String feedUrl;

    public Artikel(){}

    public String getId() { return  this.id; }
    public double getScore() { return this.score; }
    public String getCreated(){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy hh:mm");
            return dateFormat.format(this.created);

            //return dateFormat.parse(this.created.toString());
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }

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
