package com.lei2j.douyu.service.impl.es;


import com.lei2j.douyu.core.constant.DateFormatConstants;
import com.lei2j.douyu.util.DateUtil;
import com.lei2j.douyu.vo.ChatMessageVo;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by lei2j on 2018/6/4.
 */
@Component
public class ChatMessageIndex extends AbstractIndex {

    public static final String INDEX_NAME = "chat";
    public static final String TYPE_NAME = "chat";

    public ChatMessageIndex(@Autowired ElasticSearchClient client) throws IOException{
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
    protected boolean createDocumentWithMap(String id, Map<String, Object> document) {
        IndexResponse response = client.client().prepareIndex(INDEX_NAME, TYPE_NAME)
                .setId(id).setSource(document).execute().actionGet();
		int status = response.status().getStatus();
		if (status >= 300) {
			logger.error("[chat]保存数据失败status:{},record:{}", response.status(), document);
		}
        return status < 300;
    }

    @Override
    protected boolean createDocumentWithString(String id, String json) {
        IndexResponse response = client.client().prepareIndex(INDEX_NAME, TYPE_NAME)
                .setId(id).setSource(json, XContentType.JSON)
                .get();
		int status = response.status().getStatus();
		if (status >= 300) {
			logger.error("[chat]保存数据失败status:{},record:{}", response.status(), json);
		}
		return status < 300;
    }

    @Override
    protected boolean createDocumentWithBuilder(String id,Serializable document)throws IOException{
		return false;
//        IndexResponse response = client.client().prepareIndex(INDEX_NAME, TYPE_NAME)
//                .setId(id).setSource(super.builderDocument(document))
//                .get();
//        logger.info("index {} create document status:{}",response.status().getStatus());
    }

    public void createBatchDocument(List<ChatMessageVo> list) throws IOException {
		BulkRequestBuilder prepareBulk = client.client().prepareBulk();
		if (list != null) {
			for (ChatMessageVo item : list) {
				prepareBulk.add(client.client()
						.prepareIndex(INDEX_NAME, TYPE_NAME, item.getCid())
						.setSource(buildSource(item)));
			}
			BulkResponse bulkResponse = prepareBulk.get();
			if (bulkResponse.hasFailures()) {
				logger.error("执行结果:" + bulkResponse.buildFailureMessage());
				Iterator<BulkItemResponse> iterator = bulkResponse.iterator();
				while (iterator.hasNext()) {
					BulkItemResponse itemResponse = iterator.next();
					String id = itemResponse.getFailure().getId();
					logger.error("chat[id:{},插入失败,失败原因:{}]", id, itemResponse.getFailureMessage());
				}
			}
		}
	}

    protected XContentBuilder buildSource(ChatMessageVo chatMessage) throws IOException {
    	XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
    		.startObject();
    		if(chatMessage.getCid()!=null) {
    			xContentBuilder.field("cid", chatMessage.getCid());
    		}
    		if(chatMessage.getRid()!=null) {
    			xContentBuilder.field("rid", chatMessage.getRid());
    		}
    		if(chatMessage.getUid()!=null) {
    			xContentBuilder.field("uid", chatMessage.getUid());
    		}
    		if(chatMessage.getNn()!=null) {
    			xContentBuilder.field("nn", chatMessage.getNn());
    		}
    		if(chatMessage.getTxt()!=null) {
    			xContentBuilder.field("txt", chatMessage.getTxt());
    		}
    		if(chatMessage.getLevel()!=null) {
    			xContentBuilder.field("level", chatMessage.getLevel());
    		}
    		if(chatMessage.getCol()!=null) {
    			xContentBuilder.field("col", chatMessage.getCol());
    		}
    		if(chatMessage.getCt()!=null) {
    			xContentBuilder.field("ct", chatMessage.getCt());
    		}
    		if(chatMessage.getRg()!=null) {
    			xContentBuilder.field("rg", chatMessage.getRg());
    		}
    		if(chatMessage.getPg()!=null) {
    			xContentBuilder.field("pg", chatMessage.getPg());
    		}
    		if(chatMessage.getNl()!=null) {
    			xContentBuilder.field("nl", chatMessage.getNl());
    		}
    		if(chatMessage.getIc()!=null) {
    			xContentBuilder.field("ic", chatMessage.getIc());
    		}
    		if(chatMessage.getNc()!=null) {
    			xContentBuilder.field("nc", chatMessage.getNc());
    		}
    		if(chatMessage.getBnn()!=null) {
    			xContentBuilder.field("bnn", chatMessage.getBnn());
    		}
    		if(chatMessage.getBl()!=null) {
    			xContentBuilder.field("bl", chatMessage.getBl());
    		}
    		if(chatMessage.getBrid()!=null) {
    			xContentBuilder.field("brid", chatMessage.getBrid());
    		}
    		if(chatMessage.getIfs()!=null) {
    			xContentBuilder.field("ifs", chatMessage.getIfs());
    		}
    		if(chatMessage.getCreateAt()!=null) {
    			xContentBuilder.field("createAt", DateUtil.localDateTimeFormat(chatMessage.getCreateAt()));
    		}
    		xContentBuilder.endObject();
    	return xContentBuilder;
    }


    @Override
    protected XContentBuilder buildMapping() throws IOException{
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject()
                .startObject("properties")
                .startObject("cid").field("type","keyword").field("index", true).field("store", true).endObject()
                .startObject("rid").field("type", "integer").field("index", true).field("store", true).endObject()
                .startObject("uid").field("type","long").field("index",true).field("store",true).endObject()
                .startObject("nn").field("type","keyword").field("index",true).field("store",true).endObject()
                .startObject("txt").field("type","text").field("analyzer","ik_max_word").field("index",true).field("store",true).endObject()
                .startObject("level").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("col").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("ct").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("rg").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("pg").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("ic").field("type","keyword").field("index",true).field("store",true).endObject()
                .startObject("nl").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("nc").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("bnn").field("type","keyword").field("index",true).field("store",true).endObject()
                .startObject("bl").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("brid").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("ifs").field("type","integer").field("index",true).field("store",true).endObject()
                .startObject("createAt").field("format", DateFormatConstants.DATETIME_FORMAT).field("type","date").field("index",true).field("store",true).endObject()
                .endObject()
            .endObject();
        return xContentBuilder;
    }
}
