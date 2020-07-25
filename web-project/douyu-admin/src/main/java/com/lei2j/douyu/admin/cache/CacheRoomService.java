/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.admin.cache;

import com.lei2j.douyu.admin.danmu.service.DouyuLogin;

import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/19.
 */
public interface CacheRoomService {

     /**
      * 获取房间
      * @param room room
      * @return DouyuLogin
      */
     DouyuLogin get(Integer room);

     /**
      * 缓存房间
      * @param room room
      * @param value value
      */
     void cache(Integer room, DouyuLogin value);

     /**
      * 移除房间
      * @param room room
      */
     void remove(Integer room);

     /**
      * 判断是否存在
      * @param room room
      * @return boolean
      */
     boolean containsKey(Integer room);

     /**
      * 获取所有房间
      * @return Map
      */
     Map<Integer, DouyuLogin> getAll();
}
