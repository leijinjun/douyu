package com.lei2j.douyu.es.search;

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

    protected boolean createIndex(String indexName,String type,Settings.Builder settings) throws IOException{
        CreateIndexResponse indexResponse = null;
        if(!existsIndex(indexName)){
            indexResponse = client.client().admin().indices().prepareCreate(indexName)
                    .setSettings(
                            settings
                    ).addMapping(type,this.buildMapping()).get();
        }
        boolean isCreated = indexResponse != null ? indexResponse.isAcknowledged() : false;
        if(isCreated){
            logger.info("created index [{}]",indexName);
        }else {
            logger.info("exist index [{}]",indexName);
        }
        return isCreated;
    }

    public void createDocument(String id,Serializable document){
        String json = JSONObject.toJSONStringWithDateFormat(document, DateFormatConstants.DATETIME_FORMAT);
        this.createDocumentWithString(id,json);
    }

    public void createDocument(String cid,Map<String,Object> dataMap){
        this.createDocumentWithMap(cid,dataMap);
    }

    /*protected XContentBuilder builderDocument(Serializable document) throws IOException{
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(document.getClass());
        for (PropertyDescriptor var:
                propertyDescriptors) {
            if(var.getDisplayName().equals("class")){
                continue;
            }
            try {
                xContentBuilder.field(var.getName(),var.getReadMethod().invoke(document));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        xContentBuilder.endObject();
        return xContentBuilder;
    }*/

    public void close(){
        client.client().close();
    }

    public boolean existsIndex(String indexName){
        return client.client().admin().indices()
                .prepareExists(indexName).get().isExists();
    }

    abstract XContentBuilder buildMapping() throws IOException;

    abstract boolean createDocumentWithMap(String id,Map<String,Object> document) ;

    abstract boolean createDocumentWithString(String id,String json);

    abstract boolean createDocumentWithBuilder(String id, Serializable document) throws IOException;

}
