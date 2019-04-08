package com.lei2j.douyu.admin.danmu.service;

import com.lei2j.douyu.admin.message.exception.DouyuMessageReadException;
import com.lei2j.douyu.danmu.message.converter.MessageConvert;
import com.lei2j.douyu.danmu.pojo.DouyuMessage;
import com.lei2j.douyu.util.LHUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/15.
 */
public class DouyuNioConnection {
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(DouyuNioConnection.class);

	private static DouyuNioConnection douyuNIOConnection;

	private Selector selector;

	private ExecutorService singleExecutorService = Executors.newSingleThreadExecutor();

	private ByteBuffer dst = ByteBuffer.allocate(12);

	public static DouyuNioConnection initConnection() throws IOException{
		if(douyuNIOConnection==null){
			synchronized (DouyuNioConnection.class){
				if(douyuNIOConnection==null){
					douyuNIOConnection = new DouyuNioConnection();
				}
			}
		}
		return douyuNIOConnection;
	}

	private DouyuNioConnection() throws IOException {
		this.selector = Selector.open();
		start();
    }

	public void start() {
		singleExecutorService.execute(()->read());
	}

	public void write(DouyuMessage douyuMessage, DouyuNioLogin douyuNIOLogin) throws IOException {
    	byte[]  messages= MessageConvert.preConvert(douyuMessage);
    	int messageLength = messages.length;
		ByteBuffer sendBuf = ByteBuffer.allocate(messageLength);
		SocketChannel socketChannel = douyuNIOLogin.getSocketChannel();
		sendBuf.put(messages);
		sendBuf.flip();
		socketChannel.write(sendBuf);
	}
    
    public void read() {
    	while(true) {
			try {
				selector.select(3000);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectedKeys.iterator();
				while(iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					iterator.remove();
					if(selectionKey.isReadable()) {
						SocketChannel channel = (SocketChannel) selectionKey.channel();
						DouyuNioLogin attachment = (DouyuNioLogin) selectionKey.attachment();
						SocketChannel socketChannel = attachment.getSocketChannel();
						if (!socketChannel.isConnected() || !socketChannel.isOpen()) {
							selectionKey.cancel();
							continue;
						}
						try {
							byte[] data = read1(channel);
							DouyuMessage douyuMessage = MessageConvert.postConvert(data);
							attachment.dispatch(douyuMessage);
						} catch (Exception e) {
							selectionKey.cancel();
							//非正常退出
							if(socketChannel.isConnected()||socketChannel.isOpen()){
								LOGGER.error("房间{}读取消息出错",attachment.getRoom());
								e.printStackTrace();
								attachment.retry();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    private byte[] read1(SocketChannel channel) throws IOException{
		int offset = 0;
		dst.clear();
		while (true){
			int readSize = channel.read(dst);
			if(readSize==-1){
				throw new DouyuMessageReadException("server connect is closed");
			}
			offset += readSize;
			if(offset>= 12 ){
				break;
			}
		}
		dst.flip();
		byte[] var = new byte[4];
		dst.get(var, 0, 4);
		//获取本条消息总长度
		int totalLength = LHUtil.lowerToInt(var);
		dst.get(var,0,4);
		int reTotalLength = LHUtil.lowerToInt(var);
		dst.get(var,0,2);
		int msgType = LHUtil.lowerToShort(var);
		//校验消息头
		if(totalLength!=reTotalLength||msgType!=690) {
			throw new DouyuMessageReadException("server connect is closed");
		}
//			获取数据长度
		int messageLength = totalLength-8;
		byte[] data = new byte[messageLength];
		offset = 0;
		while (true){
			dst.clear();
			int remainderCount = data.length-offset;
			if(dst.remaining() >= remainderCount){
				dst.limit(remainderCount);
			}
			int readSize = channel.read(dst);
			if(readSize==-1){
				throw new DouyuMessageReadException("server connect is closed");
			}
			dst.flip();
			dst.get(data,offset,readSize);
			offset += readSize;
			if(offset >= data.length){
				break;
			}
		}
		return data;
	}

	public Selector getSelector(){
		return selector;
	}

}
