package com.lei2j.douyu.service.impl.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lei2j
 * 此类不推荐使用,根据elasticsearch未来更新计划,{@link TransportClient}在未来版本会被@Deprecated,
 * 未来推荐使用<a href="https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.2/java-rest-high.html">Java High Level REST Client</a>,
 * 此API目前提供了绝大多数的APIs,但任然有一些可能没有被添加，后续版本更新中将增加更多APIs，如果需要使用，可以配合
 * <a href="https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low.html"> low level Java REST Client with JSON request and response bodies</a>使用。
 * Created by lei2j on 2018/6/3.
 */
@Component
@Configuration
public class ElasticSearchClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClient.class);

    private final String clusterName;

    private String nodes;

    private TransportClient client;

    public ElasticSearchClient(@Value("${cluster.name}") String clusterName, @Value("${cluster.nodes}")String nodes) throws UnknownHostException {
        this.clusterName = clusterName;
        this.nodes = nodes;
        init();
    }

    public void init() throws UnknownHostException{
        Settings settings = Settings.builder().put("cluster.name", clusterName)
                //使用嗅探
//                .put("client.transport.sniff", true)
                .build();

        client = new PreBuiltTransportClient(settings);
        Set<String[]> nodes = parseNodes();
        for (String[] var1:
                nodes) {
            TransportAddress address = new TransportAddress(InetAddress.getByName(var1[0]),Integer.parseInt(var1[1]));
//            TransportAddress address = new InetSocketTransportAddress(InetAddress.getByName(var1[0]),Integer.parseInt(var1[1]));
            client.addTransportAddress(address);
        }
        LOGGER.info("elasticsearch init success......");
    }

    public TransportClient client(){
        return client;
    }

    private Set<String[]> parseNodes(){
        Set<String[]> nodesSet = new HashSet<>();
        String[] split = nodes.split(",");
        for (String s:
             split) {
            String[]  address= s.split(":");
            nodesSet.add(address);
        }
        return nodesSet;
    }
}
