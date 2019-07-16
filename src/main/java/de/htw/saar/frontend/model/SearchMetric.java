package de.htw.saar.frontend.model;

public class SearchMetric {

    private String search;
    private String date;

    public SearchMetric(String search, String date){
        this.search = search;
        this.date = date;
    }

    public String getSearch(){ return this.search; }

    public String getDate(){ return this.date; }
}
