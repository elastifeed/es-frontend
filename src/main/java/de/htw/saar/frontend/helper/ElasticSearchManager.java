package de.htw.saar.frontend.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import de.htw.saar.frontend.model.Artikel;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ElasticSearchManager
{
    public ElasticSearchManager()
    {
    }

    public void runOldES()
    {

    }

    public void run()
    {
        try
        {
            RestClient restClient = RestClient.builder(
                    new HttpHost("localhost",9200,"http")).build();

            Request request = new Request(
                    "GET",
                    "dummy/_search");

            Response response = restClient.performRequest(request);

            RequestLine requestLine = response.getRequestLine();
            HttpHost host = response.getHost();
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getHeaders();
            String responseBody = EntityUtils.toString(response.getEntity());

            // List of Artikel
            ArrayList<Artikel> artikelList = new ArrayList<Artikel>();

            // Strip JSON Doc to Data List
            JSONObject obj = new JSONObject(responseBody);
            JSONObject obj2 = obj.getJSONObject("hits");
            JSONArray obj3 = (JSONArray) obj2.get("hits");

            // run every entry
            for (int i = 0; i < obj3.length(); i++)
            {
                // Holds the Data of the Artikel
                Artikel myArtikel = new Artikel();

                // Complete Object
                JSONObject item = obj3.getJSONObject(i);

                // Data Object
                String data = item.getJSONObject("_source").toString();

                // Map basic data to object
                myArtikel = new ObjectMapper().readValue(data, Artikel.class);

                // add additional informations
                myArtikel.setId(item.getString("_id"));
                myArtikel.setScore(item.getDouble("_score"));

                // Add to list
                artikelList.add(myArtikel);
            }
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
        finally {

        }
    }

    public void showAllData(Client client)
    {
        System.out.println("Alle Datensätze \n");

        QueryBuilder qbMatchAllQuery = QueryBuilders.matchAllQuery();

        SearchResponse searchResponse = client.prepareSearch().setQuery(qbMatchAllQuery).execute().actionGet();

        printResult(searchResponse);

    }

    public void printResult(SearchResponse searchResponse)
    {
        for (SearchHit hit : searchResponse.getHits())
        {
            String value = hit.toString();

            System.out.println(value);
        }
    }

}
