package com.lei2j.douyu.login.service;

import java.util.*;

/**
 * Created by lei2j on 2018/5/28.
 */
public class MessageParse {

    /**
     * @return
     */
    public static Map<String,Object> parse(DouyuMessage douyuMessage){
    	String message = douyuMessage.getData().toString();
        Map<String, Object> messageMap = parse1(message);
        return messageMap;
    }

    /**
     * 解析key-value的Object
     * @param kv
     * @return
     */
    private static Map<String,Object> parse1(String kv){
        Map<String,Object> messageMap = new HashMap<>();
        String[] split1 = kv.split("/");
        for (String s:
                split1) {
            String[] map = s.split("@=");
            String key = map[0];
            String value = "";
            if(map.length==2){
                value = map[1];
            }
            key = replaceAll(key);
            value = replaceAll(value);
            //对象或数组对象类型
            if(value.indexOf("/")!=-1){
                //对象类型
                if(value.indexOf("@=")!=-1){
                    messageMap.put(key,parse1(value));
                }else{
                    List<Map<String,Object>> var1 = new ArrayList<>();
                    boolean flag = true;
                    for (String s1:
                            value.split("/")) {
                        s1 = replaceAll(s1);
                        //修正
                        if(s1.indexOf("/")==-1){
                            flag = false;
                            messageMap.put(key,value);
                            break;
                        }
                        var1.add(parse1(s1));
                    }
                    if(flag){
                        messageMap.put(key,var1);
                    }
                }
            }else{
                messageMap.put(key,value);
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
