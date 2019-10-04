package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.message.exception.DouyuMessageReadException;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.danmu.message.converter.MessageConvert;
import com.lei2j.douyu.danmu.pojo.DouyuMessage;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import com.lei2j.douyu.util.LHUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/15.
 */
public class DouyuNioConnection {
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(DouyuNioConnection.class);

	private static volatile DouyuNioConnection douyuNIOConnection;

	private Selector selector;

	private ExecutorService readExecutor = new ThreadPoolExecutor(
			1,
			1,
			30,
			TimeUnit.MINUTES,
			new ArrayBlockingQueue<>(1),
			new DefaultThreadFactory("thd-nio-message-read-%d", true, 5));

	private final ByteBuffer readBuf = ByteBuffer.allocateDirect(4096);

    private final ByteBuffer writeBuf = ByteBuffer.allocateDirect(1024);

	private AtomicBoolean isStart = new AtomicBoolean(false);

	private DouyuNioConnection() throws IOException {
		this.selector = Selector.open();
	}

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

	public void start() {
		readExecutor.execute(this::read);
	}

	public void write(DouyuMessage douyuMessage, SocketChannel socketChannel) throws IOException {
		byte[] messages = MessageConvert.preConvert(douyuMessage);
		int messageLength = messages.length;
		synchronized (writeBuf) {
			writeBuf.clear();
			for (int offset = 0; offset < messageLength; ) {
				int remaining = writeBuf.remaining();
				int len = messageLength - offset;
				len = len > remaining ? remaining : len;
				writeBuf.put(messages, offset, len);
				offset += len;
				//转换为读模式
				writeBuf.flip();
				socketChannel.write(writeBuf);
				writeBuf.clear();
			}
		}
	}

	public void read() {
		while (true) {
			try {
				Set<SelectionKey> keys = selector.keys();
				if (CollectionUtils.isEmpty(keys)) {
					isStart.compareAndSet(true, false);
					return;
				}
				selector.select(200);
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectedKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					iterator.remove();
					if (selectionKey.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
						DouyuNioLogin attachment = (DouyuNioLogin) selectionKey.attachment();
						if (!socketChannel.isConnected() || !socketChannel.isOpen()) {
							selectionKey.cancel();
							continue;
						}
						try {
							byte[] data = read1(socketChannel);
							DouyuMessage douyuMessage = MessageConvert.postConvert(data);
							attachment.dispatch(douyuMessage);
						} catch (Exception e) {
							selectionKey.cancel();
							//非正常退出
							if (socketChannel.isConnected() || socketChannel.isOpen()) {
								LOGGER.error("房间{}读取消息出错", attachment.getRoom());
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

	private byte[] read1(SocketChannel channel) throws IOException {
		int offset = 0;
		readBuf.clear();
		readBuf.limit(12);
		while (offset < 12) {
			int len = channel.read(readBuf);
			if (len == -1) {
				throw new DouyuMessageReadException("connect is closed");
			}
			offset += len;
		}
		//转换为读模式
		readBuf.flip();
		byte[] var = new byte[4];
		readBuf.get(var, 0, 4);
		//获取本条消息总长度
		int totalLength = LHUtil.lowerToInt(var);
		readBuf.get(var, 0, 4);
		int reTotalLength = LHUtil.lowerToInt(var);
		readBuf.get(var, 0, 2);
		int msgType = LHUtil.lowerToShort(var);
		//校验消息头
		if (totalLength != reTotalLength || msgType != 690) {
			throw new DouyuMessageReadException("server connect is closed");
		}
//		获取数据长度
		int messageLength = totalLength - 8;
		byte[] data = new byte[messageLength];
		readBuf.clear();
		for (offset = 0; offset < messageLength; ) {
			int remaining = readBuf.remaining();
			int limit = messageLength - offset;
			limit = limit > remaining ? remaining : limit;
			readBuf.limit(limit);
			int len = channel.read(readBuf);
			if (len == 0) continue;
			if (len == -1) {
				throw new DouyuMessageReadException("server connect is closed");
			}
			readBuf.flip();
			readBuf.get(data, offset, len);
			readBuf.clear();
			offset += len;
		}
		return data;
	}

	public SocketChannel register(DouyuNioLogin attr) throws IOException {
		SocketChannel socketChannel = SelectorProvider.provider().openSocketChannel();
		DouyuAddress douyuAddress = attr.getDouyuAddress();
		socketChannel.connect(new InetSocketAddress(douyuAddress.getIp(), douyuAddress.getPort()));
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ, attr);
		if (isStart.compareAndSet(false, true)) {
			start();
		}
		return socketChannel;
	}
}
