package com.lei2j.douyu.admin.cache;

import com.lei2j.douyu.admin.danmu.service.DouyuLogin;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by lei2j on 2018/8/19.
 * @author lei2j
 */
@Component
public class CacheRoomServiceImpl implements CacheRoomService {

    private final static ConcurrentMap<Integer, DouyuLogin> CACHE = new ConcurrentSkipListMap<>();

    public CacheRoomServiceImpl(){
    }

    @Override
    public DouyuLogin get(Integer room) {
        return CACHE.get(room);
    }
    @Override
    public void cache(Integer room,DouyuLogin value) {
        CACHE.put(room,value);
    }

    @Override
    public void remove(Integer room) {
        CACHE.remove(room);
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
