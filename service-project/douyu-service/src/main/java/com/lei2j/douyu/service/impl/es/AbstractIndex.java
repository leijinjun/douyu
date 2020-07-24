package com.lei2j.douyu.service.impl.es;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.constant.DateFormatConstants;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/4.
 */
public abstract class AbstractIndex {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ElasticSearchClient client;

    protected AbstractIndex(ElasticSearchClient client){
        this.client = client;
    }

    protected void createIndex(String indexName,String type,Settings.Builder settings) throws Exception{
        if (!existsIndex(indexName)) {
            CreateIndexResponse indexResponse = client.client().admin().indices().prepareCreate(indexName)
                    .setSettings(
                            settings
                    ).addMapping(type, this.buildMapping()).get();
            boolean isCreated = indexResponse.isAcknowledged();
            if (!isCreated) {
                logger.error("create index [{}] error", indexName);
                throw new Exception("create index [" + indexName + "] error");
            }
            logger.info("created index [{}]",indexName);
        }
    }

    public void createDocument(String id,Serializable document){
        String json = JSONObject.toJSONStringWithDateFormat(document, DateFormatConstants.DATETIME_FORMAT);
        this.createDocumentWithString(id,json);
    }

    public void createDocument(String cid,Map<String,Object> dataMap){
        this.createDocumentWithMap(cid,dataMap);
    }

    public void close(){
        client.client().close();
    }

    public boolean existsIndex(String indexName){
        return client.client().admin().indices()
                .prepareExists(indexName).get().isExists();
    }

    /**
     * create
     * @return xContentBuilder
     * @throws IOException create index error
     */
    protected abstract XContentBuilder buildMapping() throws IOException;

    /**
     * create
     * @param id id
     * @param document document
     * @return boolean
     */
    protected abstract boolean createDocumentWithMap(String id,Map<String,Object> document) ;

    /**
     * create
     * @param id id
     * @param json json
     * @return boolean
     */
    protected abstract boolean createDocumentWithString(String id,String json);

}
