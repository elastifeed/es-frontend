package de.htw.saar.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.saar.frontend.controller.UserController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.ArtikelNew;
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
import java.lang.reflect.Array;
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
    private final String hostname = "localhost";
    private final String scheme = "http";
    private final int port = 9200;

    private String index = "dummy_new";

    UserController userController;

    public ElasticSearchService()
    {
        /*
        if(userController.isLoggedIn()){
            this.index = userController.getCurrentUser().getUsername();
        }
        else{
            this.index = "dummy_new";
        }
         */
    }

    /**
     * Gibt einten RestClient zurück
     * @return
     */
    private RestClient getRestClient(){
        //Make the connection to ElasticSearch
        RestClient restClient = RestClient.builder(
                new HttpHost(hostname, port, scheme)).build();

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

    /**
     * Sends the prev builded request to the server and extracts the response to an readable object
     * returns an arraylist of artikel
     * @param request
     * @return
     */
    public ArrayList<ArtikelNew> executeRequestNewDataFormat (Request request)
    {
        try {
            RestClient restClient = getRestClient();
            Response response = restClient.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity());

            // List of Artikel
            ArrayList<ArtikelNew> artikelList = new ArrayList<>();

            // Strip JSON Doc to Data List
            JSONObject obj = new JSONObject(responseBody).getJSONObject("hits");
            JSONArray resultArray = (JSONArray) obj.get("hits");

            // run every entry
            for (int i = 0; i < resultArray.length(); i++) {
                // Holds the Data of the Artikel
                ArtikelNew myArtikel = new ArtikelNew();

                // Complete Object
                JSONObject item = resultArray.getJSONObject(i);

                // Data Object
                String data = item.getJSONObject("_source").toString();

                // Map basic data to object
                myArtikel = new ObjectMapper().readValue(data, ArtikelNew.class);

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


    public int getIndexSize()
    {
        try{
            Request request = new Request(
                    "GET",
                    this.index + "/_stats");

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
    public ArrayList<ArtikelNew> getArtikelPaged(int size,int from)
    {
        try {
            ArrayList<ArtikelNew> artikelArrayList = new ArrayList<>();

            Request request = new Request(
                    "GET",
                    this.index + "/_search?sort=created:desc&size=" + size + "&from=" + from);

            artikelArrayList = executeRequestNewDataFormat(request);


            // TODO REMOVE
            // Testrequest new Data Format
            ArrayList<ArtikelNew> resTest = new ArrayList<>();
            Request requestTest = new Request(
                    "GET",
                    this.index + "/_search?sort=created:desc&size=" + size + "&from=" + from);
            resTest = executeRequestNewDataFormat(requestTest);


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
    public ArrayList<ArtikelNew> getArtikelPagedDateRange(int size,int from, Date startDate, Date endDate, String search,Boolean exact,String sort)
    {
        String start=dateConverter(startDate);
        String end=dateConverter(endDate);

        try {
            ArrayList<ArtikelNew> artikelArrayList = new ArrayList<>();

            String query_string = "";
            String operator="OR";
            if(exact==true){
                operator="AND";
            }

            if(search != null && search.length() > 0)
            {
             query_string=",\n" +
                     "        {\n" +
                     "          \"query_string\": {\n" +
                     "            \"fields\": [\n" +
                     "              \"content\",\n" +
                     "              \"caption\"\n" +
                     "            ],\n" +
                     "            \"query\": \""+search+"\",\n" +
                     "            \"default_operator\": \""+operator+"\"\n" +
                     "          }\n" +
                     "        }";

            }
            String queryBody =
                    "{\n" +
                    "  \"query\": {\n" +
                    "    \"bool\": {\n" +
                    "      \"must\": [\n" +
                    "        {\n" +
                    "          \"range\": {\n" +
                    "            \"created\": {\n" +
                    "              \"gte\": \""+dateConverter(startDate)+"\",\n" +
                    "              \"lte\": \""+dateConverter(endDate)+"\"\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }"+query_string+"\n"+
                    "      ]\n" +
                    "    }\n" +
                    "  }\n" +
                    "  , \"sort\": [\n" +
                    "    {\n" +
                    "      \"created\": {\n" +
                    "        \"order\": \""+sort+"\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";


            Request request = new Request("GET",this.index + "/_search?size="+size+"&from="+from);

            request.setEntity(new NStringEntity(
                    queryBody,
                    ContentType.APPLICATION_JSON));

            artikelArrayList = executeRequestNewDataFormat(request);
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
    public ArrayList<ArtikelNew> getArtikelPagedByYearAndMonth(int size,int from, String year, String month)
    {
        try {
            ArrayList<ArtikelNew> artikelArrayList = new ArrayList<>();

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
                    this.index + "/_search?sort=created:desc&size=" + size + "&from=" + from);

            request.setEntity(new NStringEntity(
                    queryBody,
                    ContentType.APPLICATION_JSON));

            artikelArrayList = executeRequestNewDataFormat(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }


    int currentScrollValue = 0;
    public ArrayList<Artikel> getAllArtikelInIndex(int scrollsize)
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
                        this.index + "/_search?sort=created:desc&size=10000&from=" + currentScrollValue);

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


    public ArrayList<ArtikelNew> getAllEntries()
    {
        try {
            Request request = new Request(
                    "GET",
                    this.index + "/_search?sort=created:desc");

            ArrayList<ArtikelNew> artikelArrayList = executeRequestNewDataFormat(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<ArtikelNew> getFavoritEntries(){
        try{
            Request request = new Request(
                    "GET",
                    this.index + "/_search?q=starred:true&sort=created:desc&size=1000");

            ArrayList<ArtikelNew> favoritArtikelArrayList = executeRequestNewDataFormat(request);

            return favoritArtikelArrayList;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<ArtikelNew> getReadLaterEntries(){
        try{
            Request request = new Request(
                    "GET",
                    this.index + "/_search?q=read_later:true&sort=created:desc&size=1000");

            ArrayList<ArtikelNew> readlaterArtikelArrayList = executeRequestNewDataFormat(request);

            return readlaterArtikelArrayList;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<ArtikelNew> getEntriesByTextSearch(String search)
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
                    this.index + "/_search?sort=created:desc&q=" + "content" + ":" + query + "%20OR%20" + "caption:" + query);

            executeRequest(request);

            ArrayList<ArtikelNew> artikelArrayList = executeRequestNewDataFormat(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArtikelNew getArtikelById(String id)
    {
        try {
            Request request = new Request(
                    "GET",
                    this.index + "/_search?q=_id:" + id);

            MetricDataService m = new MetricDataService();
            m.addArtikelMetrik(id,this.index);
            ArrayList<ArtikelNew> artikelArrayList = executeRequestNewDataFormat(request);

            return artikelArrayList.get(0);
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
 public String dateConverter(Date date)
 {
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     cal.add(Calendar.HOUR, -2);
     date = cal.getTime();
     DateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
     DateFormat formatterTime = new SimpleDateFormat("HH:mm");
     String strDate = formatterDate.format(date);
     String strTime = formatterTime.format(date);
     return strDate+"T"+strTime;
 }

}
