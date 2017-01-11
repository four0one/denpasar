package com.bali.qo;

/**
 * Created by chenwei on 2017/1/10.
 */
public class DslObject {

    private QueryObject query;

    private Highlight highlight;

    public QueryObject getQuery() {
        return query;
    }

    public void setQuery(QueryObject query) {
        this.query = query;
    }

    public Highlight getHighlight() {
        return highlight;
    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }
}
