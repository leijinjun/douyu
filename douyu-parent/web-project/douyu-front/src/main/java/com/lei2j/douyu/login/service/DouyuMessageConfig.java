package com.lei2j.douyu.login.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.core.constant.DouyuApi;
import com.lei2j.douyu.util.HttpUtil;
import com.lei2j.douyu.util.RandomUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lei2j on 2018/11/25.
 */
class DouyuMessageConfig {

    public static DouyuMessage getLoginMessage(Integer room,String username,String password){
        DouyuMessage douyuMessage = new DouyuMessage();
        String devId = UUID.randomUUID().toString().replace("-","");
        long rt = System.currentTimeMillis()/1000;
        String vk = DigestUtils.md5Hex(rt + "7oE9nPEG9xXV69phU31FYCLUagKeYtsF" + devId);
        douyuMessage.add("type", MessageType.LOGIN).add("roomid", String.valueOf(room))
                .add("username",username).add("password",password)
                .add("ct","0").add("rt",String.valueOf(rt))
                .add("vk",vk).add("ver","20180413")
                .add("aver","118101901").add("ltkid","").add("biz","").add("stk","").add("dfl","")
                .add("devid",devId);
        return douyuMessage;
    }

    public static DouyuMessage getJoinMessage(Integer room){
        DouyuMessage douyuJoin = new DouyuMessage();
        douyuJoin.add("type",MessageType.JOIN_GROUP).add("rid",String.valueOf(room)).add("gid","-9999");
        return douyuJoin;
    }

    public static DouyuMessage getKeepliveMessage() {
        DouyuMessage douyuMessage = new DouyuMessage();
        douyuMessage.add("type", MessageType.KEEPLIVE);
        return douyuMessage;
    }

    /**
     * @return
     */
    public static DouyuMessage getLogoutMessage(){
        DouyuMessage douyuMessage = new DouyuMessage();
        douyuMessage.add("type", MessageType.LOGOUT);
        return douyuMessage;
    }

    /**
     * 获取房间弹幕登录服务器列表
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
    private static List<Map> getServerConfig(Integer room) throws UnsupportedEncodingException{
        String s = HttpUtil.get(DouyuApi.ROOM_SERVER_CONFIG.replace("{room}",String.valueOf(room)), null);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String serverConfig = URLDecoder.decode(jsonObject.getJSONObject("room_args").getString("server_config"), "utf-8");
        List<Map> mapList = JSONArray.parseArray(serverConfig, Map.class);
        return mapList;
    }

    /**
     * 从房间弹幕登录服务器组中随机获取一个登录服务器地址
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static DouyuAddress getLoginServerAddress(Integer room) throws IOException{
        List<Map> serverConfig = getServerConfig(room);
        int i = RandomUtil.getInt(serverConfig.size());
        Map<String,String> server = (Map<String,String>)serverConfig.get(i);
        String ip = server.get("ip");
        int port = Integer.parseInt(server.get("port"));
        DouyuAddress douyuAddress = new DouyuAddress(ip, port);
        return douyuAddress;
    }
}
