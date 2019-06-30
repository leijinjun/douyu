package com.lei2j.douyu.function;

import com.alibaba.fastjson.JSONObject;
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

    static <T> List<T> convertToList(Iterator<SearchHit> it,Class<T> mapperClass){
        List<T> list = new ArrayList<>(0);
        while (it.hasNext()) {
            SearchHit next = it.next();
            list.add(JSONObject.parseObject(next.getSourceAsString(), mapperClass));
        }
        return list;
    }
}
