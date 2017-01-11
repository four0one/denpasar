package com.bali.qo;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenwei on 2017/1/10.
 */
public class QueryObject {

    private Map<String, Object> match;

    @JSONField(name = "match_phrase")
    private Map<String, Object> matchPhrase;

    private FilteredObject filtered;

    public Map<String, Object> getMatch() {
        return match;
    }

    public QueryObject addMatch(String key, Object value) {
        match.put(key, value);
        return this;
    }

    public QueryObject createMatch() {
        match = new HashMap<>();
        return this;
    }

    public FilteredObject getFiltered() {
        return filtered;
    }

    public void setFiltered(FilteredObject filtered) {
        this.filtered = filtered;
    }

    public Map<String, Object> getMatchPhrase() {
        return matchPhrase;
    }

    public QueryObject addMatchPhrase(String key, Object value) {
        matchPhrase.put(key, value);
        return this;
    }

    public QueryObject createMatchPhrase() {
        matchPhrase = new HashMap<>();
        return this;
    }
}
