package de.htw.saar.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.saar.frontend.model.Artikel;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import java.util.ArrayList;
import java.util.Calendar;

public class ElasticSearchService
{
    public ElasticSearchService()
    { }

    /**
     * Gibt einten RestClient zurück
     * @return
     */
    private RestClient getRestClient(){
        //Make the connection to ElasticSearch
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")).build();

        return restClient;
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
            ArrayList<Artikel> artikelList = new ArrayList<>();

            // Strip JSON Doc to Data List
            JSONObject obj = new JSONObject(responseBody).getJSONObject("hits");
            JSONArray resultArray = (JSONArray) obj.get("hits");

            // run every entry
            for (int i = 0; i < resultArray.length(); i++) {
                // Holds the Data of the Artikel
                Artikel myArtikel = new Artikel();

                // Complete Object
                JSONObject item = resultArray.getJSONObject(i);

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

    /**
     * Gibt die Artikel zwischen dem Datumsbereich zurück
     * @param size
     * @param from
     * @param startDate
     * @param endDate
     * @return
     */
    public ArrayList<Artikel> getArtikelPagedDateRange(int size,int from, String startDate, String endDate)
    {
        try {
            ArrayList<Artikel> artikelArrayList = new ArrayList<>();

            // request Query
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("created").from(startDate).to(endDate.toString());
            String queryBody = "{\n \"query\": " + rangeQueryBuilder.toString() + " \n}";

            Request request = new Request(
                    "GET",
                    "dummy/_search?sort=created:desc&size=" + size + "&from=" + from);

            request.setEntity(new NStringEntity(
                    queryBody,
                    ContentType.APPLICATION_JSON));

            artikelArrayList = executeRequest(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Gibt eine Auflistung von Artikeln sortiert nach aktualistät zurück ab der gwünschten position
     * @param size
     * @param from
     * @return
     */
    public ArrayList<Artikel> getArtikelPagedByYearAndMonth(int size,int from, String year, String month)
    {
        try {
            ArrayList<Artikel> artikelArrayList = new ArrayList<>();

            // add leading 0 to month if < 10
            String convertedMonthNumber = String.format("%02d", Integer.parseInt(month));

            String startDate = year + "-" + convertedMonthNumber + "-01";

            // generate the end date of the requested month
            LocalDate endDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            endDate = endDate.withDayOfMonth(
                    endDate.getMonth().length(endDate.isLeapYear()));

            // request Query
            RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("created").from(startDate).to(endDate.toString());
            String queryBody = "{\n \"query\": " + rangeQueryBuilder.toString() + " \n}";

            Request request = new Request(
                    "GET",
                    "dummy/_search?sort=created:desc&size=" + size + "&from=" + from);

            request.setEntity(new NStringEntity(
                    queryBody,
                    ContentType.APPLICATION_JSON));

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
                    "dummy/_search?sort=created:desc&q=" + "content" + ":" + query + "%20OR%20" + "caption:" + query);

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
}
