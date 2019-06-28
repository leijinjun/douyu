package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.danmu.pojo.DouyuMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lei2j on 2018/5/28.
 */
public class MessageParse {

    /**
     * @return Map
     */
    public static Map<String,Object> parse(DouyuMessage douyuMessage){
    	String message = douyuMessage.getData().toString();
        Map<String, Object> messageMap = parse1(message);
        return messageMap;
    }

    /**
     * 解析key-value的Object
     * @param kv kv
     * @return Map
     */
    private static Map<String,Object> parse1(String kv) {
        Map<String, Object> messageMap = new HashMap<>();
        String[] split1 = kv.split("/");
        for (String s :
                split1) {
            String[] map = s.split("@=");
            String key = map[0];
            String value = "";
            if (map.length == 2) {
                value = map[1];
            }
            key = replaceAll(key);
            value = replaceAll(value);
            //对象或数组对象类型
            if (value.contains("/")) {
                //对象类型
                if (value.contains("@=")) {
                    messageMap.put(key, parse1(value));
                } else {
                    List<Map<String, Object>> var1 = new ArrayList<>();
                    boolean flag = true;
                    for (String s1 :
                            value.split("/")) {
                        s1 = replaceAll(s1);
                        //修正
                        if (!s1.contains("/")) {
                            flag = false;
                            messageMap.put(key, value);
                            break;
                        }
                        var1.add(parse1(s1));
                    }
                    if (flag) {
                        messageMap.put(key, var1);
                    }
                }
            } else {
                messageMap.put(key, value);
            }
        }
        return messageMap;
    }

    public static String replaceAll(String str){
        if(str.contains("@S")){
            str = str.replaceAll("@S","/");
        }
        if(str.contains("@A")){
            str = str.replaceAll("@A","@");
        }
        return str;
    }

}
