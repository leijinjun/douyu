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

package com.lei2j.douyu.admin.danmu.serialization;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by lei2j on 2018/5/28.
 */
public class STTDouyuMessage {

    /**
     * @return Map
     */
    public static Map<String, Object> deserialize(byte[] data) {
        Objects.requireNonNull(data);
        String message = new String(data, Charset.forName("utf-8"));
        Map<String, Object> messageMap = parse1(message);
        return messageMap;
    }

    /**
     * 解析key-value的Object
     * @param kv kv
     * @return Map
     */
    private static Map<String, Object> parse1(String kv) {
        Map<String, Object> messageMap = new HashMap<>(16);
        String[] split1 = kv.split("/");
        for (String var1 : split1) {
            if (var1.equals("\u0000") || var1.equals("")) continue;
            String[] map = var1.split("@=");
            String key = map[0];
            String value = map.length == 2 ? map[1] : "";
            key = reverseEscape(key);
            value = reverseEscape(value);
            //非对象或数组类型
            if (!value.endsWith("/")) {
                messageMap.put(key, value);
            } else {
                //对象类型
                if (value.contains("@=")) {
                    messageMap.put(key, parse1(value));
                } else {
                    //数组类型
                    List<Object> list = new ArrayList<>(16);
                    String[] sp1 = value.split("/");
                    for (String var2 : sp1) {
                        if (var2.equals("\u0000") || var2.equals("")) continue;
                        //数组元素为对象类型
                        if (var2.endsWith("@S")) {
                            var2 = reverseEscape(var2);
                            list.add(parse1(var2));
                        } else {
                            //普通值
                            list.add(var2);
                        }
                    }
                    messageMap.put(key, list);
                }
            }
        }
        return messageMap;
    }

    private static String reverseEscape(String s) {
        s = s.replaceAll("@S", "/");
        s = s.replaceAll("@A", "@");
        return s;
    }
}
