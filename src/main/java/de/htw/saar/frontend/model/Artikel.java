package de.htw.saar.frontend.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Artikel {

    private String id;
    private double score;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date created;
    private String caption;
    private String content;
    private String url;
    private Boolean isFromFeed;
    private String feedUrl;
    private String thumbnail;
    private String pdf;
    private boolean favorite;
    private boolean spaeteransehen;

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

    public String getDomain()
    {
        try {

            if (this.url.length() < 1){
                return "ERROR";
            }

            URI uri = new URI(this.url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception ex){
            return "ERROR";
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

    public String getThumbnailsmall() {

            // return reandom image
            Random r = new Random();
            int rng = r.nextInt((2000 - 1) + 1) + 1;

            return "https://picsum.photos/225/180?random=" + rng;
    }

    public String getThumbnail() {

        if(this.thumbnail == null || this.thumbnail.length() < 1)
        {
            // return reandom image
            Random r = new Random();
            int rng = r.nextInt((2000 - 1) + 1) + 1;

            return "https://picsum.photos/1110/250?random=" + rng;
        }
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean getHasPdf() {
        if(pdf == null || pdf.length() < 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getPdfName() {

        if(pdf == null || pdf.length() < 1)
        {
            return "PDF not available";
        }
        else
        {
            return "Download PDF";
        }
    }

    public String getPdf() {

        if(pdf == null || pdf.length() < 1)
        {
            return "#";
        }

        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isSpaeteransehen() {
        return spaeteransehen;
    }

    public void setSpaeteransehen(boolean spaeteransehen) {
        this.spaeteransehen = spaeteransehen;
    }
}
