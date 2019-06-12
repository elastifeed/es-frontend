package de.htw.saar.frontend.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.saar.frontend.model.Artikel;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.util.ArrayList;

public class ElasticSearchManager
{
    public ElasticSearchManager()
    { }

    public void run()
    {
        try {
            //Make the connection to ElasticSearch
            RestClient restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http")).build();

            //Gets all the entries form ElasticSearch
            getAllEntries(restClient);

            //Gets the filtered entries from ElasticSearch
            String field = "caption";
            String search = "Datenleck";
            getFilteredEntries(restClient, field, search);

            search = "Harter";
            getFilteredEntries(restClient, field, search);

            //Closes the connection to ElasticSearch
            restClient.close();
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<Artikel> getAllEntries(RestClient restClient) throws Exception
    {
        Request request = new Request(
                "GET",
                "dummy/_search");

        executeRequest(restClient, request);

        ArrayList<Artikel> artikelArrayList = executeRequest(restClient, request);

        printArrayList(artikelArrayList);

        return artikelArrayList;
    }

    public void getFilteredEntries(RestClient restClient, String field, String search) throws Exception
    {
        Request request = new Request(
                "GET",
                "dummy/_search?q="+field+":"+search);

        executeRequest(restClient, request);

        ArrayList<Artikel> artikelArrayList = executeRequest(restClient, request);

        printArrayList(artikelArrayList);
    }


    public ArrayList<Artikel> executeRequest (RestClient restClient, Request request) throws Exception
    {
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        // List of Artikel
        ArrayList<Artikel> artikelList = new ArrayList<Artikel>();

        // Strip JSON Doc to Data List
        JSONObject obj = new JSONObject(responseBody);
        JSONObject obj2 = obj.getJSONObject("hits");
        JSONArray obj3 = (JSONArray) obj2.get("hits");

        // run every entry
        for (int i = 0; i < obj3.length(); i++) {
            // Holds the Data of the Artikel
            Artikel myArtikel = new Artikel();

            // Complete Object
            JSONObject item = obj3.getJSONObject(i);

            // Data Object
            String data = item.getJSONObject("_source").toString();

            // Map basic data to object
            myArtikel = new ObjectMapper().readValue(data, Artikel.class);

            // Add additional informations
            myArtikel.setId(item.getString("_id"));
            myArtikel.setScore(item.getDouble("_score"));

            // Add to list
            artikelList.add(myArtikel);
        }

        return artikelList;
    }

    public void printArrayList(ArrayList<Artikel> artikelArrayList)
    {
        for(Artikel artikel : artikelArrayList) {
            System.out.println(">>>>>> ID: " + artikel.getId() + "<<<<<<");
            System.out.println(">>>>>> Score: " + artikel.getScore() + "<<<<<<");
            System.out.println(">>>>>> Created: " + artikel.getCreated() + "<<<<<<");
            System.out.println(">>>>>> Caption: " + artikel.getCaption() + "<<<<<<");
            System.out.println(">>>>>> Content: " + artikel.getContent() + "<<<<<<");
            System.out.println(">>>>>> URL: " + artikel.getUrl() + "<<<<<<");
            System.out.println(">>>>>> IsFromFeed: " + artikel.getIsFromFeed() + "<<<<<<");
            System.out.println(">>>>>> FeedUrl: " + artikel.getFeedUrl() + "<<<<<<");
        }
    }
}
