package de.htw.saar.frontend.model;

public class ArtikelMetric {

    private String artikelTitel;
    private String date;

    public ArtikelMetric(String artikelTitel, String date){
        this.artikelTitel = artikelTitel;
        this.date = date;
    }

    public String getArtikelTitel(){ return this.artikelTitel; }

    public String getDate(){ return this.date; }
}
