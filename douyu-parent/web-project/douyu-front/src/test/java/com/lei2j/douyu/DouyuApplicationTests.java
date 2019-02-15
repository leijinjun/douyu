package com.lei2j.douyu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.ApplicationContextUtil;
import com.lei2j.douyu.es.search.ChatMessageIndex;
import com.lei2j.douyu.es.search.GiftIndex;
import com.lei2j.douyu.login.service.DouyuNormalLogin;
import com.lei2j.douyu.service.ExecutorTaskService;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Map;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DouyuApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DouyuApplicationTests.class);

    @Autowired
    private ChatMessageIndex chatMessageIndex;
    @Autowired
    private  GiftIndex giftIndex;
    @Autowired
    private ApplicationContextUtil applicationContextUtil;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test2()throws IOException{
        HttpGet httpGet = new HttpGet();
        URI uri = URI.create("http://open.douyucdn.cn/api/RoomApi/room/"+485503);
        httpGet.setURI(uri);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        BufferedInputStream buffIn = new BufferedInputStream(entity.getContent());
        StringBuilder sb = new StringBuilder();
        byte[] b = new byte[2048];
        int len;
        while ((len=buffIn.read(b))!=-1){
            sb.append(new String(b,0,len,"utf-8"));
        }
        JSONObject json = JSONObject.parseObject(sb.toString());
        Map<String,Map<String,Object>> giftMap = new HashMap<>();
        if(json.getIntValue("error")==0){
            JSONObject dataJSON = json.getJSONObject("data");
            JSONArray giftArr = dataJSON.getJSONArray("gift");
            for (int i = 0; i < giftArr.size(); i++) {
                JSONObject var1 = giftArr.getJSONObject(i);
                if(var1.getIntValue("type")==2){
                    giftMap.put(var1.getString("id"),var1);
                }
            }
        }
        System.out.println(Double.parseDouble(String.valueOf(giftMap.get(String.valueOf(1695)).get("pc"))));
    }

    @Test
    public void test3()throws IOException{
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8880));
        ByteBuffer out = ByteBuffer.allocate(2048);
        ByteBuffer in = ByteBuffer.allocate(1024);
        out.put("hello server".getBytes());
        socketChannel.write(out);
        socketChannel.read(in);
        System.out.println(new String(in.array()));
        socketChannel.close();
    }

    @Test
    public void test4()throws IOException{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel = serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8880));
        ByteBuffer in = ByteBuffer.allocate(1024);
        ByteBuffer out =ByteBuffer.allocate(1024);
        out.put("hello client".getBytes());
        Selector selector = Selector.open();
        while (true){
            SocketChannel socketChannel  = serverSocketChannel.accept();
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
            socketChannel.read(in);
            System.out.println(new String(in.array()));
            socketChannel.write(out);
        }

    }

    @Test
    public void test5() throws Exception{
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(32);
        buffer.put((byte) 1);
        FileInputStream fin = new FileInputStream("/prod.properties");
        FileChannel channel = fin.getChannel();
        ByteBuffer b = ByteBuffer.allocate(64);
        int len=0;
        while((len=channel.read(b))!=-1){
            System.out.println(new String(b.array(),0,len));
            b.clear();
        }
    }

    @Test
    public void test6(){
        ByteBuffer b = ByteBuffer.allocate(64);
        b.put("d0".getBytes());
        byte[] dst=new byte[1024];
        b.flip();
        b.get(1);
        System.out.println(b.toString());
        b.compact();
//        while (b.hasRemaining()){
////            b.get(dst,0,b.remaining());
////            b.get();
//            System.out.println(new String(dst));
//        }
        System.out.println(b.toString());
    }

    @Test
    public void test7() throws IOException{
        DouyuNormalLogin douyuNormalLogin = new DouyuNormalLogin(485503);
        douyuNormalLogin.login();
        //115.231.106.21,chat
        //114.118.20.37 login
    }

}
