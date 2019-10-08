package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.danmu.message.DouyuMessage;
import com.lei2j.douyu.admin.danmu.protocol.DouyuMessageProtocol;
import com.lei2j.douyu.admin.danmu.serialization.STTDouyuMessage;
import com.lei2j.douyu.admin.danmu.transport.DouyuTransport;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.thread.factory.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lei2j
 * Created by lei2j on 2018/8/15.
 */
public class DouyuNioConnection {
	
	private static final Logger LOGGER  = LoggerFactory.getLogger(DouyuNioConnection.class);

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
		this.selector = createSelector();
	}

	private Selector createSelector() throws IOException {
		return Selector.open();
	}

	public static DouyuNioConnection createConnection() throws IOException {
		return new DouyuNioConnection();
	}

	public void start() {
		readExecutor.execute(this::eventLoop);
	}

	public void write(DouyuMessage douyuMessage, SocketChannel socketChannel) throws IOException {
		byte[] message = DouyuMessageProtocol.encode(douyuMessage);
		synchronized (writeBuf) {
			DouyuTransport.write(message, writeBuf, socketChannel);
		}
	}

	private void eventLoop() {
		while (true) {
			Set<SelectionKey> keys = selector.keys();
			if (CollectionUtils.isEmpty(keys)) {
				isStart.compareAndSet(true, false);
				return;
			}
			try {
				selector.select(200);
			} catch (IOException e) {
				LOGGER.error("[NioConnection.read]selector i/o error", e.getCause());
				try {
					resetSelector(selector);
					continue;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			Selector curSelector = this.selector;
			Set<SelectionKey> selectedKeys = curSelector.selectedKeys();
			Iterator<SelectionKey> iterator = selectedKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				iterator.remove();
				SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
				if (!(selectionKey.isValid() && socketChannel.isOpen())) {
					selectionKey.cancel();
					continue;
				}
				if (selectionKey.isReadable()) {
					final DouyuNioLogin attachment = (DouyuNioLogin) selectionKey.attachment();
					try {
						Map<String, Object> dataMap = read(socketChannel);
						attachment.dispatch(dataMap);
					} catch (Exception e) {
						selectionKey.cancel();
						synchronized (attachment) {
							if (!socketChannel.isOpen()) {
								continue;
							}
						}
						try {
							socketChannel.close();
						} catch (IOException ignore) {
						}
						Integer room = attachment.getRoom();
						LOGGER.error("[DouyuNio.read]读取消息错误，room:{}", room, e.fillInStackTrace());
						attachment.retry();
					}
				}
			}
		}
	}

	private void resetSelector(Selector oldSelector) throws IOException{
		Selector curSelector = createSelector();
		this.selector = curSelector;
		if (oldSelector.isOpen()) {
			oldSelector.keys().parallelStream().forEach(selectionKey ->
			{
				SocketChannel channel = (SocketChannel) selectionKey.channel();
				selectionKey.cancel();
				DouyuNioLogin attachment = (DouyuNioLogin) selectionKey.attachment();
				try {
					register(attachment, channel);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			try {
				oldSelector.close();
			} catch (IOException ignore) {
			}
		}
	}

	public Map<String, Object> read(SocketChannel channel) throws IOException {
		byte[] data = DouyuMessageProtocol.decode(readBuf, channel);
		return STTDouyuMessage.deserialize(data);
	}

	public SelectionKey register(DouyuNioLogin attr,SocketChannel socketChannel) throws IOException {
		DouyuAddress douyuAddress = attr.getDouyuAddress();
		if (!socketChannel.isConnected()) {
			socketChannel.connect(new InetSocketAddress(douyuAddress.getIp(), douyuAddress.getPort()));
			socketChannel.configureBlocking(false);
		}
		SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ, attr);
		if (isStart.compareAndSet(false, true)) {
			start();
		}
		return selectionKey;
	}

	/**
	 * 返回该Connection已连接SocketChannel数量
	 * @return SocketChannel Size
	 */
	public int getChannelLength() {
		if (!selector.isOpen()) {
			throw new ClosedSelectorException();
		}
		Set<SelectionKey> keys = selector.keys();
		return keys == null ? 0 : keys.size();
	}
}
