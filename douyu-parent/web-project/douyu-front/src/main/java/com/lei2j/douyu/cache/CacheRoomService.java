package com.lei2j.douyu.cache;

import java.util.Map;

import com.lei2j.douyu.login.service.DouyuLogin;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public interface CacheRoomService {

     /**
      * 获取房间
      * @param room
      * @return
      */
     DouyuLogin get(Integer room);

     /**
      * 缓存房间
      * @param room
      * @param value
      */
     void cache(Integer room, DouyuLogin value);

     /**
      * 移除房间
      * @param room
      */
     void remove(Integer room);

     boolean containsKey(Integer room);

     /**
      * 获取所有房间
      * @return
      */
     Map<Integer,DouyuLogin> getAll();
}
