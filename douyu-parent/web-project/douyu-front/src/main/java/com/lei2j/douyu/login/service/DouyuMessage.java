package com.lei2j.douyu.login.service;

import java.util.List;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/27.
 */
public class DouyuMessage {

    private StringBuilder data = new StringBuilder();

    public DouyuMessage() {
    }

    public DouyuMessage(String data) {
        this.data.append(data);
    }

    public DouyuMessage add(String key, String value){
        if(key==null||value==null){
            return this;
        }
        key = replaceAll(key);
        value = replaceAll(value);
        data.append(key).append("@=").append(value).append("/");
        return this;
    }

    public DouyuMessage addArray(String key,List<String> dataArray){
        if(dataArray==null){
            return this;
        }
        key = replaceAll(key);
        data.append(key).append("@=");
        for (String s:
             dataArray) {
            s = replaceAll(s);
            data.append(s).append("/");
        }
        return this;
    }

    public void append(String data){
        this.data.append(data);
    }

    public StringBuilder getData(){
        return data;
    }

    private String replaceAll(String data){
        if(data.contains("/")){
            data = data.replaceAll("/","@S");
        }
        if(data.contains("@")){
            data = data.replaceAll("@","@A");
        }
        return data;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DouyuMessage{");
        sb.append("data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
