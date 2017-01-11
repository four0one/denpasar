package com.bali.qo;

import com.bali.compare.CompareOption;
import com.bali.compare.Gt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenwei on 2017/1/10.
 */
public class FilterObject {

    private Map<String, Object> range = new HashMap<>();

    public FilterObject createRange() {
        range = new HashMap<>();
        return this;
    }

    public Map<String, Object> getRange() {
        return range;
    }

    public void addRange(String key, Object value, String compare) {
        if (compare.equals(CompareOption.GT)) {
            Gt gt = new Gt();
            gt.gt(value);
            range.put(key, gt);
        }
    }
}
