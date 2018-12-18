package com.lei2j.douyu.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/27.
 */
@Configuration
public class DouyuAddress {

    @Value("${douyu.danmu.url}")
    private String ip;
    @Value("${douyu.danmu.port}")
    private Integer port;

    public DouyuAddress() {
    }

    public DouyuAddress(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DouyuAddress{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", port=").append(port);
        sb.append('}');
        return sb.toString();
    }
}
