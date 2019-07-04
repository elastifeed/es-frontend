package de.htw.saar.frontend.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.saar.frontend.model.Artikel;
import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.util.ArrayList;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class ElasticSearchManager
{
    public ElasticSearchManager()
    { }

    private RestClient getRestClient(){
        //Make the connection to ElasticSearch
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")).build();

        return restClient;
    }

    public int getIndexSize(String index)
    {
        try{
            Request request = new Request(
                    "GET",
                    index + "/_stats");

            RestClient restClient = getRestClient();
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            // Strip JSON Doc to Data List
            JSONObject obj = new JSONObject(responseBody);
            JSONObject resultObj = obj.getJSONObject("_all").getJSONObject("primaries").getJSONObject("docs");

            return resultObj.getInt("count");
        }
        catch (Exception ex)
        {
            return 0;
        }
    }

    /**
     * Gibt eine Auflistung von Artikeln sortiert nach aktualistät zurück ab der gwünschten position
     * @param size
     * @param from
     * @return
     */
    public ArrayList<Artikel> getArtikelPaged(int size,int from)
    {
        try {
            ArrayList<Artikel> artikelArrayList = new ArrayList<Artikel>();

                Request request = new Request(
                        "GET",
                        "dummy/_search?sort=created:desc&size=" + size + "&from=" + from);

                artikelArrayList = executeRequest(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }


    int currentScrollValue = 0;
    public ArrayList<Artikel> getAllArtikelInIndex(String index, int scrollsize)
    {
        if(scrollsize > 10000)
        {
            scrollsize = 10000;
        }

        boolean hasResults = true;
        try {
            ArrayList<Artikel> artikelArrayList = new ArrayList<Artikel>();

            while(hasResults)
            {
                Request request = new Request(
                        "GET",
                        index + "/_search?sort=created:desc&size=10000&from=" + currentScrollValue);

                ArrayList<Artikel> tmpList;
                tmpList = executeRequest(request);

                if(tmpList.size() < scrollsize)
                    hasResults = false;

                tmpList.forEach(x -> artikelArrayList.add(x));
                currentScrollValue += scrollsize;
            }

            int result = 0;
            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }



    public ArrayList<Artikel> getAllEntries()
    {
        try {
            Request request = new Request(
                    "GET",
                    "dummy/_search?sort=created:desc");

            ArrayList<Artikel> artikelArrayList = executeRequest(request);

            printArrayList(artikelArrayList);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<Artikel> getFilteredEntries(String field, String search)
    {
        try {
            Request request = new Request(
                    "GET",
                    "dummy/_search?q=" + field + ":" + search);

            executeRequest(request);

            ArrayList<Artikel> artikelArrayList = executeRequest(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<Artikel> getEntriesByTextSearch(String search)
    {
        try {
            String[] arr = search.split(" ");
            String query = "";

            int counter = 0;
            for(String s : arr)
            {
                if(counter != 0)
                {
                    query += "%20OR%20";
                }
                query += s;
                counter++;
            }

            Request request = new Request(
                    "GET",
                    "dummy/_search?q=" + "content" + ":" + query + "%20OR%20" + "caption:" + query);

            executeRequest(request);

            ArrayList<Artikel> artikelArrayList = executeRequest(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }


    public Artikel getArtikelById(String id)
    {
        try {
            Request request = new Request(
                    "GET",
                    "dummy/_search?q=_id:" + id);

            ArrayList<Artikel> artikelArrayList = executeRequest(request);

            return artikelArrayList.get(0);
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Sends the prev builded request to the server and extracts the response to an readable object
     * returns an arraylist of artikel
     * @param request
     * @return
     */
    public ArrayList<Artikel> executeRequest (Request request)
    {
        try {
            RestClient restClient = getRestClient();
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


                // Add to list
                artikelList.add(myArtikel);
            }

            return artikelList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }

    //Ausgabe zum Test
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
