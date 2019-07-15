package de.htw.saar.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.saar.frontend.controller.UserController;
import de.htw.saar.frontend.model.Artikel;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    public ArrayList<Artikel> executeRequestNewDataFormat (Request request)
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
            restClient.close();
            return artikelList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Performs a raw request to the server
     * no response expected
     * @param request
     */
    public void performRawRequest(Request request)
    {
        try {
            RestClient restClient = getRestClient();
            restClient.performRequest(request);
            restClient.close();
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    /**
     * returns the size of the given index
     * @return
     */
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
            restClient.close();
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
    public ArrayList<Artikel> getArtikelPaged(int size, int from)
    {
        try {
            ArrayList<Artikel> artikelArrayList = new ArrayList<>();

            Request request = new Request(
                    "GET",
                    this.index + "/_search?sort=created:desc&size=" + size + "&from=" + from);

            artikelArrayList = executeRequestNewDataFormat(request);

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
    public ArrayList<Artikel> getArtikelPagedDateRange(int size, int from, Date startDate, Date endDate, String search, Boolean exact, String sort)
    {
        String start=dateConverter(startDate);
        String end=dateConverter(endDate);

        try {
            ArrayList<Artikel> artikelArrayList = new ArrayList<>();

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
                     "              \"raw_content\",\n" +
                     "              \"title\"\n" +
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
    public ArrayList<Artikel> getArtikelPagedByYearAndMonth(int size, int from, String year, String month)
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


    public ArrayList<Artikel> getAllEntries()
    {
        try {
            Request request = new Request(
                    "GET",
                    this.index + "/_search?sort=created:desc");

            ArrayList<Artikel> artikelArrayList = executeRequestNewDataFormat(request);

            return artikelArrayList;
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<Artikel> getFavoritEntries(){
        try{
            Request request = new Request(
                    "GET",
                    this.index + "/_search?q=starred:true&sort=created:desc&size=1000");

            ArrayList<Artikel> favoritArtikelArrayList = executeRequestNewDataFormat(request);

            return favoritArtikelArrayList;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public ArrayList<Artikel> getReadLaterEntries(){
        try{
            Request request = new Request(
                    "GET",
                    this.index + "/_search?q=read_later:true&sort=created:desc&size=1000");

            ArrayList<Artikel> readlaterArtikelArrayList = executeRequestNewDataFormat(request);

            return readlaterArtikelArrayList;
        }catch (Exception ex){
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
                    this.index + "/_search?sort=created:desc&q=" + "raw_content" + ":" + query + "%20OR%20" + "title:" + query);

            ArrayList<Artikel> artikelArrayList = executeRequestNewDataFormat(request);

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
                    this.index + "/_search?q=_id:" + id);

            MetricDataService m = new MetricDataService();
            m.addArtikelMetrik(id,this.index);
            ArrayList<Artikel> artikelArrayList = executeRequestNewDataFormat(request);

            return artikelArrayList.get(0);
        }catch(Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * toggles favorite status of the artikel
     * @param id
     * @return
     */
    public void toggleFavorite(String id, boolean setTrue)
    {
        String queryBody = "{\n" +
                "    \"doc\": {";

        if(setTrue){
            queryBody += "\"starred\": true";
        } else{
            queryBody += "\"starred\": false";
        }

        queryBody += "    }\n" +
                "}";

        Request request = new Request(
                "POST",
                "/" + this.index + "/_update/" + id);

        request.setEntity(new NStringEntity(
                queryBody,
                ContentType.APPLICATION_JSON));

        performRawRequest(request);
    }

    /**
     * toggles read_later status of the artikel
     * @param id
     * @return
     */
    public void toggleReadLater(String id, boolean setTrue)
    {
        String queryBody = "{\n" +
                "    \"doc\": {";

        if(setTrue){
            queryBody += "\"read_later\": true";
        } else{
            queryBody += "\"read_later\": false";
        }

        queryBody += "    }\n" +
                "}";

        Request request = new Request(
                "POST",
                "/" + this.index + "/_update/" + id);

        request.setEntity(new NStringEntity(
                queryBody,
                ContentType.APPLICATION_JSON));

        performRawRequest(request);
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
