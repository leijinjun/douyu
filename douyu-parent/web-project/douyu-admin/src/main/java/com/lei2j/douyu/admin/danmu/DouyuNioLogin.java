package com.lei2j.douyu.admin.danmu;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;

/**
 * @author lei2j
 */
public class DouyuNioLogin extends AbstractDouyuLogin {
	
	private SocketChannel socketChannel;

	private DouyuNioConnection douyuNioConnection;

	public DouyuNioLogin(Integer room) throws IOException {
		super(room);
		this.douyuNioConnection = DouyuNioConnection.initConnection();
	}

	/**
	 * @throws IOException IOException
	 */
	@Override
	public int login() throws IOException {
		DouyuDanmuLoginAuth danmuLoginAuth = getChatServerAddress();
		if (danmuLoginAuth == null) {
			return -1;
		}
		this.douyuAddress = danmuLoginAuth.getAddress();
		String username = danmuLoginAuth.getUsername();
		logger.info("开始连接弹幕服务器:{}:{}", douyuAddress.getIp(), douyuAddress.getPort());
		this.socketChannel = douyuNioConnection.register(this);
		douyuNioConnection.write(DouyuMessageConfig.getLoginMessage(room, username, "1234567890123456"), this.socketChannel);
		join();
		if (keepaliveSchedule != null) {
			keepaliveSchedule.cancel();
		}
		keepaliveSchedule = new KeepaliveSchedule();
		keepaliveSchedule.schedule(() -> {
			try {
				if (!this.socketChannel.isConnected() || !this.socketChannel.isOpen()) {
					logger.info("房间{}|心跳检测停止", room);
					keepaliveSchedule.cancel();
					return;
				}
				douyuNioConnection.write(DouyuMessageConfig.getKeepaliveMessage(), this.socketChannel);
				logger.info("房间{}|发送心跳检测{}", room, LocalDateTime.now());
			} catch (Exception e) {
				keepaliveSchedule.cancel();
				logger.info("房间{}|心跳检测停止", room);
				if (this.socketChannel.isOpen() || this.socketChannel.isConnected()) {
					e.printStackTrace();
				}
			}
		});
		return 1;
	}

	private void join()throws IOException{
        //加入房间分组
		DouyuNioConnection.initConnection().write(DouyuMessageConfig.getJoinMessage(room),socketChannel);
		logger.info("房间{}|连接成功",room);
    }
	
	@Override
	public void logout() {
		try {
			douyuNioConnection.write(DouyuMessageConfig.getLogoutMessage(),socketChannel);
			socketChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			keepaliveSchedule.cancel();
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
			login();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
