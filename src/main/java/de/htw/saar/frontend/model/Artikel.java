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
    private String author;
    private String title;
    private String raw_content;
    private String markdown_content;
    private String pdf;
    private String screenshot;
    private String thumbnail;
    private String url;
    private boolean from_feed;
    private String feed_url;
    private boolean starred;
    private boolean read_later;


    public Artikel()
    {}

    public String getId() { return  this.id; }
    public void setId(String id) {
        this.id = id;
    }

    public double getScore() { return this.score; }
    public void setScore(double score) {
        this.score = score;
    }

    public Date getCreated(){ return this.created; }
    public void setCreated(Date created) {this.created = created;}

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


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRaw_content() {
        return raw_content;
    }

    public void setRaw_content(String raw_content) {
        this.raw_content = raw_content;
    }

    public String getMarkdown_content() {
        return markdown_content;
    }

    public void setMarkdown_content(String markdown_content) {
        this.markdown_content = markdown_content;
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

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
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

    public String getUrl(){ return this.url; }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFrom_feed() {
        return from_feed;
    }

    public void setFrom_feed(boolean from_feed) {
        this.from_feed = from_feed;
    }

    public String getFeed_url() {
        return feed_url;
    }

    public void setFeed_url(String feed_url) {
        this.feed_url = feed_url;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isRead_later() {
        return read_later;
    }

    public void setRead_later(boolean read_later) {
        this.read_later = read_later;
    }
}
