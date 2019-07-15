package de.htw.saar.frontend.service;

import de.htw.saar.frontend.model.Artikel;

import java.util.ArrayList;

public class ArtikelService
{
    ElasticSearchService manager = new ElasticSearchService();


    public int getArtikelPageCount()
    {
        return manager.getIndexSize();
    }

    public ArrayList<Artikel> getArtikelPaged(int size, int from)
    {
        return manager.getArtikelPaged(size,from);
    }

    public ArrayList<Artikel> getAllArtikel()
    {
        try {
            return manager.getAllEntries();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<Artikel> getArtikelBySearch(String query)
    {
        try {
            return manager.getEntriesByTextSearch(query);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
