package de.htw.saar.frontend.helper;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.net.InetAddress;

public class ElasticSearchManager
{
    public ElasticSearchManager()
    {
    }

    public void run()
    {
        Client client = null;

        try
        {
            client = new PreBuiltTransportClient(Settings.builder().put("client.transport.sniff", true)
                        .put("cluster.name", "elasticsearch").build())
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

            showAllData(client);


            /**
            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("host1"), 9300));

           client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

            client = TransportClient.builder().settings(Settings.settingsBuilder().put("elasticsearch").build()).build();

            val settings = Settings.settingsBuilder().put("cluster.name", cluster).build()

            client = TransportClient.builder().settings(Settings.settingsBuilder().put("elasticsearch").build()).build()
            transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port.toInt))
            */
        }
        catch(Exception ex) {
            System.out.println(ex);
        }
        finally {
            client.close();
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
