package com.lei2j.douyu.functions;

import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.vo.ChatMessageVo;
import com.lei2j.douyu.vo.GiftVo;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * author: lei2j
 * date: 2019/3/31
 */
public interface IndexSearchConvert<T> {


    List<T> convert(Iterator<SearchHit> iterator);

    static List<ChatMessageVo> convertToChatMessageVo(Iterator<SearchHit> it){
        return convertToList(it,ChatMessageVo.class);
    }

    static List<GiftVo> convertToGiftVo(Iterator<SearchHit> it){
        return convertToList(it,GiftVo.class);
    }

    static <T> List<T> convertToList(Iterator<SearchHit> it,Class<T> mapperClass){
        List<T> list = new ArrayList<>(0);
        while (it.hasNext()) {
            SearchHit next = it.next();
            list.add(JSONObject.parseObject(next.getSourceAsString(), mapperClass));
        }
        return list;
    }
}
