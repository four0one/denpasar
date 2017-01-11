package com.bali.qo.builder;

import com.bali.qo.DslObject;
import com.bali.qo.FilteredObject;
import com.bali.qo.QueryObject;

/**
 * Created by chenwei on 2017/1/11.
 */
public class DslBuilder {

    private DslObject dslObject;

    private QueryObject queryObject;

    private FilteredObject filtered;

    public DslBuilder createDsl(){
        dslObject = new DslObject();
        return this;
    }

    public DslBuilder createQuery(){
        queryObject = new QueryObject();
        return this;
    }


}
