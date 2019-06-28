package com.lei2j.douyu.cache;

import com.lei2j.douyu.danmu.service.DouyuLogin;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lei2j on 2018/8/19.
 * @author lei2j
 */
@Component
public class CacheRoomServiceImpl implements CacheRoomService {

    private static Map<Integer,DouyuLogin> cache = new ConcurrentHashMap<>();

    public CacheRoomServiceImpl(){
    }

    @Override
    public DouyuLogin get(Integer room) {
        if(cache.containsKey(room)){
            DouyuLogin douyuLogin = cache.get(room);
            return douyuLogin;
        }
        return null;
    }
    @Override
    public void cache(Integer room,DouyuLogin value) {
        if(!cache.containsKey(room)){
            cache.put(room,value);
        }
    }

    @Override
    public void remove(Integer room) {
        if(containsKey(room)){
            cache.remove(room);
        }
    }

    @Override
    public boolean containsKey(Integer room) {
        return cache.containsKey(room);
    }

    @Override
    public Map<Integer, DouyuLogin> getAll() {
        return cache;
    }
}
