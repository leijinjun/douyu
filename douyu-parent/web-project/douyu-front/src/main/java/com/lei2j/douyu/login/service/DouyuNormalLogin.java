package com.lei2j.douyu.login.service;

import com.lei2j.douyu.login.exception.DouyuMessageReadException;
import com.lei2j.douyu.web.util.DouyuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/28.
 */
public class DouyuNormalLogin extends AbstractDouyuLogin {

    private static final Logger LOGGER = LoggerFactory.getLogger(DouyuNormalLogin.class);

    private DouyuConnection douyuConnection;

    /**
     * 弹幕读取线程
     */
    private ExecutorService executorService = Executors.newScheduledThreadPool(1,Executors.defaultThreadFactory());

    public DouyuNormalLogin(Integer room) throws IOException {
        super(room);
    }

    @Override
    public void init() {
        super.roomDetail = DouyuUtil.getRoomDetail(room);
    }

    /**
     * 登录
     *
     * @throws IOException
     */
    @Override
    public int login() throws IOException {
        DouyuDanmuLoginAuth danmuLoginAuth = super.getChatServerAddress();
        if (danmuLoginAuth == null){
            return -1;
        }
        this.douyuAddress = danmuLoginAuth.getAddress();
        String username = danmuLoginAuth.getUsername();
        LOGGER.info("开始连接弹幕服务器:{}:{}", douyuAddress.getIp(), douyuAddress.getPort());
        this.douyuConnection = DouyuConnection.initConnection(douyuAddress);
        douyuConnection.write(DouyuMessageConfig.getLoginMessage(room, username, "1234567890123456"));
        LOGGER.info("房间|{},连接弹幕服务器成功", room);
        join();
        if (keepliveSchedule != null) {
            keepliveSchedule.cancel();
        }
        //启动心跳检测
        keepliveSchedule = new KeepliveSchedule();
        keepliveSchedule.schedule(()->keeplive());
        //开始执行弹幕读取
        executorService.execute(() ->{
            int f = 1;
            while (f == 1) {
                f = read();
            }
            //logout,do nothing
            if(f == 0) {
                return;
            }
            //read error
            if (f == -1) {
                retry();
            }
        });
        return 1;
    }

    /**
     * 加入房间分组
     *
     * @throws IOException
     */
    private void join() throws IOException {
        //加入房间分组
        douyuConnection.write(DouyuMessageConfig.getJoinMessage(room));
        LOGGER.info("房间|{},开始加入房间分组", room);
        Map<String, Object> messageMap = MessageParse.parse(douyuConnection.read());
        String type = "type";
        String error = "error";
        if (error.equals(messageMap.get(type))) {
            LOGGER.error("房间|{},加入分组失败,错误信息:{}", messageMap);
            return;
        }
        LOGGER.info("房间{}|加入分组成功", room);
    }
    
    private int read() {
        try {
            DouyuMessage douyuMessage = this.douyuConnection.read();
            if (douyuMessage != null) {
                super.dispatch(douyuMessage);
                return 1;
            }
        } catch (IOException |DouyuMessageReadException e) {
            //非正常退出
			if (douyuConnection.isClosed()) {
				return 0;
			} else {
				LOGGER.error("房间|{}读取消息异常",room);
				e.printStackTrace();
			}
        }
        return -1;
    }

    /**
     * 退出房间
     *
     * @throws IOException
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
            keepliveSchedule.cancel();
            cacheRoomService.remove(room);
            logger.info("房间{}|成功退出",room);
        }
    }

    /**
     * 重新登录
     */
    @Override
    public void retry() {
        LOGGER.info("重新登录房间:{}", room);
        try {
			if (!douyuConnection.isClosed()) {
				douyuConnection.close();
			}
            this.init();
            this.login();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 发送心跳
     */
    protected void keeplive() {
        if(douyuConnection.isClosed()){
            logger.info("房间{}|心跳检测停止",room);
            keepliveSchedule.cancel();
        }
        DouyuMessage keepliveMessage = DouyuMessageConfig.getKeepliveMessage();
        try {
            douyuConnection.write(keepliveMessage);
            LOGGER.info("房间|{}发送心跳检测,时间:{}", this.room, LocalDateTime.now().toString());
        } catch (Exception e) {
            keepliveSchedule.cancel();
            logger.info("房间{}|心跳检测停止", room);
            if(!douyuConnection.isClosed()) {
            	e.printStackTrace();
            }
        }
    }
}
