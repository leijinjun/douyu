package com.lei2j.douyu.cache;

import com.lei2j.douyu.danmu.service.DouyuLogin;

import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public interface CacheRoomService {

     /**
      * 获取房间
      * @param room roomId
      * @return DouyuLogin
      */
     DouyuLogin get(Integer room);

     /**
      * 缓存房间
      * @param room roomId
      * @param value value
      */
     void cache(Integer room, DouyuLogin value);

     /**
      * 移除房间
      * @param room roomId
      */
     void remove(Integer room);

     boolean containsKey(Integer room);

     /**
      * 获取所有房间
      * @return Map
      */
     Map<Integer,DouyuLogin> getAll();
}
