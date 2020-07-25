/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.im;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author lei2j
 */
public class Server {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private Selector selector;

    private WorkHandler workHandler;

    private int port;

    private ServerSocketChannel serverSocketChannel;
    
    public Server(int port) throws IOException {
    	this.port = port;
		init();
	}

    private void init() throws IOException{
    	LOGGER.info(">>>>>>>>>>>>>>server start<<<<<<<<<<<<<");
		this.selector = Selector.open();
        serverSocketChannel = SelectorProvider.provider().openServerSocketChannel();
        serverSocketChannel.bind(new InetSocketAddress(port));
		LOGGER.info(">>>>>>>>>>>>>>server port:{} bounded<<<<<<<<<<<<<",port);
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
		LOGGER.info(">>>>>>>>>>>>>>server inited<<<<<<<<<<<<<");
	}

	public void work(){
		MainListen masterListenThread = new MainListen(this);
		masterListenThread.start();
		this.workHandler = new WorkHandler();
		workHandler.eventLoop();
	}
    
    private void listen() throws IOException {
    	while(true) {
    		selector.selectNow();
    		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
    		while(it.hasNext()) {
    			SelectionKey selectionKey = it.next();
    			it.remove();
    			if(selectionKey.isAcceptable()) {
    				ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
					SocketChannel socketChannel = channel.accept();
					workHandler.register(socketChannel);
    			}
    		}
    		
    	}
    }

	public int getPort() {
		return port;
	}

	private class MainListen extends Thread{

    	private Server chatConnectServer;

    	private MainListen(Server chatConnectServer){
    		this.chatConnectServer=chatConnectServer;
		}

		@Override
		public void run() {
			try {
				LOGGER.info(">>>>>>>>>>>>>>Server waiting for the client connection<<<<<<<<<<<<<");
				chatConnectServer.listen();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static class WorkHandler{

    	private static final Logger LOGGER = LoggerFactory.getLogger(WorkHandler.class);

//    	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(30,50,30, TimeUnit.MINUTES,new LinkedBlockingDeque<>(1000));

    	private static Selector clientSelector;

		private ByteBuffer buf = ByteBuffer.allocate(1024);

    	private WorkHandler() {
			try {
				clientSelector = Selector.open();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void register(SocketChannel clientSocketChannel){
			try {
				LOGGER.info("client connect ip:{}",clientSocketChannel.getRemoteAddress());
				if(check(clientSocketChannel)){
					clientSocketChannel.configureBlocking(false);
					clientSocketChannel.register(clientSelector, SelectionKey.OP_READ);
				}
			} catch (IOException e) {
				e.printStackTrace();
				LOGGER.error("client connect faild");
			}
		}

		public boolean check(SocketChannel channel){
			try {
				read(channel);
				LOGGER.info("client IP:{} rejected",channel.getRemoteAddress());
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		public int read(SocketChannel channel){
			try {
				
				buf.clear();
				int size = channel.read(buf);
				if (size == -1) {
					LOGGER.info("client closed ip:{}", channel.getRemoteAddress());
					channel.close();
					return -1;
				}
				buf.flip();
				byte[] dst = new byte[size];
				buf.get(dst,0,size);
				System.out.println(new String(dst,0,size));
				return size;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}

		public int write(SocketChannel channel) {
    		if(!channel.isOpen()){
    			return 0;
			}
			try {
				buf.clear();
				buf.put("hello".getBytes(Charset.forName("utf-8")));
				buf.flip();
				return channel.write(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}

		public void eventLoop(){
			while (true){
				try {
					clientSelector.selectNow();
					Iterator<SelectionKey> it = clientSelector.selectedKeys().iterator();
					while (it.hasNext()){
						SelectionKey selectionKey = it.next();
						it.remove();
						if(selectionKey.isReadable()){
							SocketChannel channel = (SocketChannel) selectionKey.channel();
							read(channel);
							write(channel);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
}
