package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.cache.CacheRoomService;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * author: 98611
 * date: 2019/10/5
 */

@Configuration
public class DouyuWorker {

    private DouyuNioWorkEventLoop workEventLoop = new DouyuNioWorkEventLoop();

    @Resource
    private CacheRoomService cacheRoomService;

    public DouyuWorker() throws IOException {
    }

    /**
     * @param room  房间ID
     * @return 1 room login success,-1 room login fail
     * @throws IOException login error
     */
    public int login(Integer room) throws IOException {
        DouyuNioLogin douyuNioLogin = workEventLoop.get(room);
        if (douyuNioLogin.login()) {
            cacheRoomService.cache(room, douyuNioLogin);
            return 1;
        }
        return -1;
    }
}
