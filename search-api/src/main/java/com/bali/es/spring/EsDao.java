package com.bali.es.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by chenwei on 2017/1/12.
 */
@Component
public class EsDao extends ElasticsearchRepository<> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    public void createType(){
        IndexQuery indexQuery = new IndexQuery();
        elasticsearchTemplate.index()
    }

}
