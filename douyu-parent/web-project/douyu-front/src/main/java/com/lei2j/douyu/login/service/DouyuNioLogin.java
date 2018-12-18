package com.lei2j.douyu.login.service;

import com.lei2j.douyu.web.util.DouyuUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class DouyuNioLogin extends AbstractDouyuLogin {
	
	private SocketChannel socketChannel;

	private DouyuNioConnection douyuNConnection;

	public DouyuNioLogin(Integer room) throws IOException {
		super(room);
		this.douyuNConnection = DouyuNioConnection.initConnection();
	}

	@Override
	public void init() {
		super.roomDetail = DouyuUtil.getRoomDetail(room);
	}

	private void register() throws IOException{
		this.socketChannel = SelectorProvider.provider().openSocketChannel();
		socketChannel.connect(new InetSocketAddress(douyuAddress.getIp(), douyuAddress.getPort()));
		socketChannel.configureBlocking(false);
		socketChannel.register(douyuNConnection.getSelector(),SelectionKey.OP_READ,this);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	@Override
	public int login() throws IOException {
		DouyuDanmuLoginAuth danmuLoginAuth = getChatServerAddress();
		if (danmuLoginAuth == null){
			return -1;
		}
		this.douyuAddress = danmuLoginAuth.getAddress();
		String username = danmuLoginAuth.getUsername();
		logger.info("开始连接弹幕服务器:{}:{}", douyuAddress.getIp(), douyuAddress.getPort());
		register();
		douyuNConnection.write(DouyuMessageConfig.getLoginMessage(room,username,"1234567890123456"), this);
		join();
		if (keepliveSchedule != null) {
			keepliveSchedule.cancel();
		}
		keepliveSchedule = new KeepliveSchedule();
		keepliveSchedule.schedule(()->keeplive());
		return 1;
	}

	private void join()throws IOException{
        //加入房间分组
		DouyuNioConnection.initConnection().write(DouyuMessageConfig.getJoinMessage(room),this);
		logger.info("房间{}|连接成功",room);
    }
	
	@Override
	public void logout() {
		try {
			douyuNConnection.write(DouyuMessageConfig.getLogoutMessage(),this);
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			keepliveSchedule.cancel();
			cacheRoomService.remove(room);
			logger.info("房间{}|成功退出",room);
		}
	}

	@Override
	public void retry(){
		try {
			if (socketChannel.isConnected() || socketChannel.isOpen()) {
				socketChannel.close();
			}
			logger.info("重新连接房间:{}",room);
			init();
			login();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void keeplive() {
		try {
			if(!socketChannel.isConnected() || !socketChannel.isOpen()) {
				logger.info("房间{}|心跳检测停止",room);
				keepliveSchedule.cancel();
				return;
			}
			douyuNConnection.write(DouyuMessageConfig.getKeepliveMessage(),this);
			logger.info("房间{}|发送心跳检测{}",room, LocalDateTime.now());
		} catch (Exception e){
			keepliveSchedule.cancel();
			logger.info("房间{}|心跳检测停止",room);
			if(socketChannel.isOpen()||socketChannel.isConnected()){
				e.printStackTrace();
			}
		}
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

}
