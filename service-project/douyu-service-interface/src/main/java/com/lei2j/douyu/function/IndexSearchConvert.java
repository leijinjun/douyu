/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

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
@FunctionalInterface
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
