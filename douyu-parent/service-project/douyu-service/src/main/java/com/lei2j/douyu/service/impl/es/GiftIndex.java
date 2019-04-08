package com.lei2j.douyu.service.impl.es;

import com.lei2j.douyu.core.constant.DateFormatConstants;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/6/4.
 */
@Component
public class GiftIndex extends AbstractIndex {

    public static final String INDEX_NAME = "gift_v1";
    public static final String TYPE_NAME = "gift";

    public GiftIndex(@Autowired ElasticSearchClient client) throws IOException{
        super(client);
        Settings.Builder settings = Settings.builder()
//                .put("client.transport.sniff",true)
                //node分片数量
                .put("index.number_of_shards", 5)
                //主分片的副本分片数量
                .put("index.number_of_replicas", 1);
        createIndex(INDEX_NAME,TYPE_NAME,settings);
    }

    @Override
    public boolean createDocumentWithMap(String id, Map<String, Object> document) {
        IndexResponse response = client.client().prepareIndex(INDEX_NAME, TYPE_NAME)
                .setId(id).setSource(document, XContentType.JSON)
                .get();
        int status = response.status().getStatus();
        if(status >= 300){
            logger.info("[gift]保存数据失败status:{},record:{}",response.status(),document);
        }
        return status < 300;
    }

    @Override
    public boolean createDocumentWithString(String id, String json) {
        IndexResponse response = client.client().prepareIndex(INDEX_NAME, TYPE_NAME)
                .setId(id)
                .setSource(json, XContentType.JSON)
                .get();
        int status = response.status().getStatus();
        if(status >= 300){
            logger.info("[gift]保存数据失败status:{},record:{}",response.status(),json);
        }
        return status < 300;
    }

    @Override
    protected boolean createDocumentWithBuilder(String id, Serializable giftVO) throws IOException {
        return false;
//        IndexResponse response = client.client().prepareIndex(INDEX_NAME, TYPE_NAME)
//                .setId(id).setSource(super.builderDocument(giftVO))
//                .get();
//        logger.info("index [{}] create document status:{}",INDEX_NAME,response.status().getStatus());
    }

    @Override
    protected XContentBuilder buildMapping() throws IOException{
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject()
                .startObject("properties")
                .startObject("id").field("type","keyword").field("index",true).field("store",true).endObject()
                .startObject("rid").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("gfid").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("gs").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("uid").field("type","long").field("index",true).field("store",true).endObject()
                .startObject("nn").field("type","keyword").field("index",true).field("store",true).endObject()
                .startObject("ic").field("type","keyword").field("index",false).field("store",true).endObject()
                .startObject("level").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("bg").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("hits").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("createAt").field("type","date").field("format", DateFormatConstants.DATETIME_FORMAT).field("index",true).field("store",true).endObject()
                .startObject("gfcnt").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("pc").field("type","double").field("index",true).field("store",true).endObject()
                .endObject()
                .endObject()
                ;
        return xContentBuilder;
    }

    public XContentBuilder upsertMapping(String field,String type,boolean isIndex) throws IOException{
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject()
                .startObject("properties")
                .startObject(field).field("type",type).field("index",isIndex).endObject()
                .endObject()
                .endObject();
        return xContentBuilder;
    }
}
