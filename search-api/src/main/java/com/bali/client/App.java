package com.bali.client;

import jdk.nashorn.internal.codegen.types.Type;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

/**
 * Created by chenwei on 2017/1/11.
 */
public class App {

    public static void main(String[] args) {

        Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "serdemo").build();
        Client client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress
                ("192.168.54.131",
                        9300));
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("appApkFileName", "西游");
        SearchResponse searchResponse = client.prepareSearch("appstore").setTypes("app").setQuery(queryBuilder)
                .execute().actionGet();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        System.out.println(totalHits);


    }
}
