package de.htw.saar.frontend.service;

import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.ArtikelNew;

import java.util.ArrayList;

public class ArtikelService
{
    ElasticSearchService manager = new ElasticSearchService();


    public int getArtikelPageCount()
    {
        return manager.getIndexSize();
    }

    public ArrayList<ArtikelNew> getArtikelPaged(int size, int from)
    {
        return manager.getArtikelPaged(size,from);
    }

    public ArrayList<ArtikelNew> getAllArtikel()
    {
        try {
            return manager.getAllEntries();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<ArtikelNew> getArtikelBySearch(String query)
    {
        try {
            return manager.getEntriesByTextSearch(query);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
