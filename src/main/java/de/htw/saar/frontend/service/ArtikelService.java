package de.htw.saar.frontend.service;

import de.htw.saar.frontend.helper.ElasticSearchManager;
import de.htw.saar.frontend.model.Artikel;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

import java.util.ArrayList;

public class ArtikelService
{
    ElasticSearchManager manager = new ElasticSearchManager();

    public ArrayList<Artikel> getAllArtikel()
    {
        try {
            //Make the connection to ElasticSearch
            RestClient restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http")).build();
            return manager.getAllEntries(restClient);
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }
}
