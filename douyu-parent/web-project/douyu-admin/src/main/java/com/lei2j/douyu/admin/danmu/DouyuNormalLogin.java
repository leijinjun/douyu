package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.danmu.config.DouyuMessageConfig;
import com.lei2j.douyu.admin.danmu.message.DouyuMessage;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/28.
 */
public class DouyuNormalLogin extends AbstractDouyuLogin {

    private DouyuConnection douyuConnection;

    /**
     * 弹幕读取线程
     */
    private ExecutorService executorService = Executors.newScheduledThreadPool(1, new DefaultThreadFactory("thd-normal-message-read-%d", true, 10));

    public DouyuNormalLogin(Integer room) {
        super(room);
    }

    /**
     * 登录
     *
     * @throws IOException IOException
     */
    @Override
    public boolean login() throws IOException {
        DouyuDanmuLoginAuth danmuLoginAuth = super.getChatServerAddress();
        if (danmuLoginAuth == null){
            return false;
        }
        this.douyuAddress = danmuLoginAuth.getAddress();
        String username = danmuLoginAuth.getUsername();
        logger.info("开始连接弹幕服务器:{}:{}", douyuAddress.getIp(), douyuAddress.getPort());
        this.douyuConnection = DouyuConnection.initConnection(douyuAddress);
        douyuConnection.write(DouyuMessageConfig.getLoginMessage(room, username, "1234567890123456"));
        logger.info("房间|{},连接弹幕服务器成功", room);
        if (!join()) {
            return false;
        }
        if (keepaliveSchedule != null) {
            keepaliveSchedule.cancel();
        }
        //启动心跳检测
        keepaliveSchedule = new KeepaliveSchedule();
        keepaliveSchedule.schedule(()->{
            if(douyuConnection.isClosed()){
                logger.info("房间{}|心跳检测停止",room);
                keepaliveSchedule.cancel();
            }
            DouyuMessage keepaliveMessage = DouyuMessageConfig.getKeepaliveMessage();
            try {
                douyuConnection.write(keepaliveMessage);
                logger.info("房间|{}发送心跳检测,时间:{}", this.room, LocalDateTime.now().toString());
            } catch (Exception e) {
                keepaliveSchedule.cancel();
                logger.info("房间{}|心跳检测停止", room);
                if(!douyuConnection.isClosed()) {
                    e.printStackTrace();
                }
            }
        });
        //开始执行弹幕读取
        executorService.execute(() ->{
            int f = 1;
            for (; f == 1; ) {
                f = read();
            }
            //logout,do nothing
            if(f == 0) {
                cacheRoomService.remove(room);
                return;
            }
            //read error
            if (f == -1) {
                retry();
            }
        });
        return true;
    }

    @Override
    protected DouyuDanmuLoginAuth getChatServerAddress(String username, String password) throws IOException {
        DouyuAddress douyuAddress = DouyuMessageConfig.getLoginServerAddress(room);
        DouyuConnection douyuConnection = DouyuConnection.initConnection(douyuAddress);
        douyuConnection.write(DouyuMessageConfig.getLoginMessage(room,username,password));
        Map<String, Object> loginMessageMap = douyuConnection.read();
        Map<String, Object> addressMap = douyuConnection.read();
        douyuConnection.close();
        return getLoginAuth(loginMessageMap,addressMap);
    }

    /**
     * 加入房间分组
     *
     * @throws IOException IOException
     */
    private boolean join() throws IOException {
        //加入房间分组
        douyuConnection.write(DouyuMessageConfig.getJoinMessage(room));
        logger.info("房间|{},开始加入房间分组", room);
        Map<String, Object> messageMap = douyuConnection.read();
        String type = "type";
        String error = "error";
        if (error.equals(messageMap.get(type))) {
            logger.error("房间|{},加入分组失败,错误信息:{}", messageMap);
            return false;
        }
        logger.info("房间{}|加入分组成功", room);
        return true;
    }
    
    private int read() {
        try {
            Map<String, Object> dataMap = douyuConnection.read();
            super.dispatch(dataMap);
            return 1;
        } catch (Exception e) {
            //非正常退出
			if (douyuConnection.isClosed()) {
				return 0;
			} else {
                logger.error("房间|{}读取消息异常",room);
				e.printStackTrace();
			}
        }
        return -1;
    }

    /**
     * 退出房间
     *
     */
    @Override
    public void logout() {
        try {
            douyuConnection.write(DouyuMessageConfig.getLogoutMessage());
            douyuConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            keepaliveSchedule.cancel();
            cacheRoomService.remove(room);
            logger.info("房间{}|成功退出",room);
        }
    }

    /**
     * 重新登录
     */
    @Override
    public void retry() {
        logger.info("重新登录房间:{}", room);
        try {
			if (!douyuConnection.isClosed()) {
				douyuConnection.close();
			}
            this.login();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
