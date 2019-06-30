package com.lei2j.douyu.admin.cache;

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

    private final static Map<Integer,DouyuLogin> CACHE = new ConcurrentHashMap<>();

    public CacheRoomServiceImpl(){
    }

    @Override
    public DouyuLogin get(Integer room) {
        if(CACHE.containsKey(room)){
            DouyuLogin douyuLogin = CACHE.get(room);
            return douyuLogin;
        }
        return null;
    }
    @Override
    public void cache(Integer room,DouyuLogin value) {
        if(!CACHE.containsKey(room)){
            synchronized (CACHE){
                if(!CACHE.containsKey(room)){
                    CACHE.put(room,value);
                }
            }
        }
    }

    @Override
    public void remove(Integer room) {
        if(CACHE.containsKey(room)){
            CACHE.remove(room);
        }
    }

    @Override
    public boolean containsKey(Integer room) {
        return CACHE.containsKey(room);
    }

    @Override
    public Map<Integer, DouyuLogin> getAll() {
        return CACHE;
    }
}
