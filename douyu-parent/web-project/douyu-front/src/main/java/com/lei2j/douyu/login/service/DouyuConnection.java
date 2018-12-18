package com.lei2j.douyu.login.service;

import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.login.converter.MessageConvert;
import com.lei2j.douyu.login.exception.DouyuMessageReadException;
import com.lei2j.douyu.util.LHUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/28.
 */
public class DouyuConnection {

    private static final Logger LOGGER  = LoggerFactory.getLogger(DouyuConnection.class);

    private Socket socket;
    private BufferedInputStream in;
    private BufferedOutputStream out;

    private DouyuConnection(){}

    public static DouyuConnection initConnection(DouyuAddress douyuAddress) throws IOException {
        DouyuConnection douyuConnection = new DouyuConnection();
        SocketAddress socketAddress = new InetSocketAddress(douyuAddress.getIp(), douyuAddress.getPort());
        Socket socket = new Socket();
        socket.setKeepAlive(true);
        socket.setReceiveBufferSize(104857600);
        socket.setSoTimeout(15000);
        socket.connect(socketAddress,6000);
        douyuConnection.socket = socket;
        douyuConnection.in = new BufferedInputStream(socket.getInputStream());
        douyuConnection.out = new BufferedOutputStream(socket.getOutputStream());
        return douyuConnection;
    }

    public void write(DouyuMessage douyuMessage){
        //处理header
        byte[]  messages= MessageConvert.preConvert(douyuMessage);
        try {
            if(isClosed()||socket.isOutputShutdown()){
                LOGGER.warn("connect is closed");
                return;
            }
            out.write(messages);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public DouyuMessage read() throws IOException{
        byte[] header = new byte[12];
        header = read1(header);
        if(header==null){
        	throw new DouyuMessageReadException("connection is closed");
        }
        //获取本条消息总长度
        int totalLength = LHUtil.lowerToInt(Arrays.copyOfRange(header,0,4));
        //重复本次消息长度
        int reTotalLength = LHUtil.lowerToInt(Arrays.copyOfRange(header,4,8));
        short msgType = LHUtil.lowerToShort(Arrays.copyOfRange(header, 8, 10));
        //消息校验
        short MSG_TYPE = 690;
        if(totalLength!=reTotalLength||msgType!=MSG_TYPE) {
        	LOGGER.error("获取消息错误,{}",totalLength);
        	throw new DouyuMessageReadException("connection is closed");
        }
        int messageLength = totalLength-8;
        byte[] msg = new byte[messageLength];
        msg = read1(msg);
        if(msg==null){
        	throw new DouyuMessageReadException("connection is closed");
        }
        DouyuMessage resultMessage = MessageConvert.postConvert(msg);
        return resultMessage;
    }

    private byte[] read1(byte[] dst) throws IOException{
    	int offset = 0;
    	int len = dst.length;
    	while(true){
            int size = in.read(dst,offset,len-offset);
            if(size==-1){
            	throw new DouyuMessageReadException("server connection is closed");
            }
            offset += size;
            if(offset > len-1){
                break;
            }
        }
    	return dst;
    }

    public void close() {
        if (!isClosed()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isClosed(){
        return socket.isClosed();
    }

}
