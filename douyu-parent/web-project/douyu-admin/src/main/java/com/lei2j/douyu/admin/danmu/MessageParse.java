package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.danmu.pojo.DouyuMessage;

import java.util.*;

/**
 * Created by lei2j on 2018/5/28.
 */
public class MessageParse {

    /**
     * @return Map
     */
    public static Map<String,Object> parse(DouyuMessage douyuMessage){
    	String message = douyuMessage.getData();
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
            key = replaceAll(key);
            value = replaceAll(value);
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
                            var2 = replaceAll(var2);
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

    private static String replaceAll(String s){
        return s.replaceAll("@S","/").replaceAll("@A","@");
    }
}
