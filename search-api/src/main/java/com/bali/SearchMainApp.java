package com.bali;

import com.alibaba.fastjson.JSONObject;
import com.bali.qo.*;

/**
 * Hello world!
 */
public class SearchMainApp {

    public static void main(String[] args) {
//        List<NameValuePair> payPairs = new ArrayList<NameValuePair>();
//        payPairs.add(new BasicNameValuePair("q", "name:欢乐麻将"));
//        ApiConnector.get("http://192.168.54.131:9200/appstore/app/_search?pretty", payPairs);
//
//        ApiConnector.postJson("http://192.168.54.131:9200/appstore/app/_search?pretty",
//                JSONObject.toJSONString(compositeMatch()));

        ApiConnector.postJson("http://192.168.54.131:9200/appstore/app/_search?pretty",
                JSONObject.toJSONString(matchQuery()));

    }

    private static DslObject matchQuery() {
        DslObject dslObject = new DslObject();
        QueryObject query = new QueryObject();
        query.createMatch().addMatch("intro", "大话西游 全民");
//        query.createMatchPhrase().addMatchPhrase("intro", "一句话");
        dslObject.setQuery(query);

        Highlight highlight = new Highlight();
        highlight.createFields().addFields("intro");
        dslObject.setHighlight(highlight);
        System.out.println(JSONObject.toJSONString(dslObject));
        return dslObject;
    }

    private static DslObject compositeMatch() {
        /**
         * query_top
         */
        QueryObject query = new QueryObject();

        /**
         * filtered
         */
        FilteredObject filteredObject = new FilteredObject();
        FilterObject filterObject = new FilterObject();
        filterObject.createRange().addRange("score", 20, "gt");
        filteredObject.setFilter(filterObject);

        /**
         * query_inline
         */
        QueryObject queryInline = new QueryObject();
        queryInline.createMatch().addMatch("name", "全民");
        filteredObject.setQuery(queryInline);

        query.setFiltered(filteredObject);

        DslObject dslObject = new DslObject();
        dslObject.setQuery(query);

        System.out.println(JSONObject.toJSONString(dslObject));
        return dslObject;
    }
}
