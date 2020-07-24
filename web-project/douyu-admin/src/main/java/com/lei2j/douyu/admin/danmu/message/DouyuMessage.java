package com.lei2j.douyu.admin.danmu.message;

import java.util.List;
import java.util.Optional;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/27.
 */
public class DouyuMessage {

    private StringBuilder sb = new StringBuilder();

    public DouyuMessage() {
    }

    public DouyuMessage add(String key, String value){
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        key = replaceAll(key);
        value = replaceAll(value);
        sb.append(key).append("@=").append(value).append("/");
        return this;
    }

    public DouyuMessage addArray(String key, List<String> valueArray){
        if (key == null || valueArray == null) {
            throw new NullPointerException();
        }
        key = replaceAll(key);
        sb.append(key).append("@=");
        Optional<String> optional =
                valueArray.stream().reduce((var1, var2) -> replaceAll(var1).concat("/").concat(replaceAll(var2)));
        sb.append(replaceAll(optional.orElse("")+"/"));
        sb.append("/");
        return this;
    }

    public void setResult(String message) {
        sb.append(message);
    }

    public String getData() {
        return sb.toString();
    }

    private String replaceAll(String data) {
        data = data.replaceAll("@", "@A");
        data = data.replaceAll("/", "@S");
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("DouyuMessage{");
        result.append(sb);
        result.append('}');
        return result.toString();
    }
}
