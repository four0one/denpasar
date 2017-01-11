package com.bali.qo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenwei on 2017/1/10.
 */
public class Highlight {

    private Map<String, Object> fields;

    private static final Object EMPTY_OBJECT = new Object();

    public Map<String, Object> getFields() {
        return fields;
    }

    public Highlight createFields() {
        fields = new HashMap<>();
        return this;
    }

    public Highlight addFields(String field) {
        fields.put(field, EMPTY_OBJECT);
        return this;
    }
}
