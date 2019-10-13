package com.lei2j.douyu.admin.danmu;

import com.lei2j.douyu.admin.danmu.message.DouyuMessage;
import com.lei2j.douyu.admin.danmu.protocol.DouyuMessageProtocol;
import com.lei2j.douyu.admin.danmu.serialization.STTDouyuMessage;
import com.lei2j.douyu.admin.danmu.exception.DouyuMessageReadException;
import com.lei2j.douyu.core.config.DouyuAddress;
import com.lei2j.douyu.util.LHUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/28.
 */
class DouyuConnection {

    private static final Logger LOGGER  = LoggerFactory.getLogger(DouyuConnection.class);

    private Socket socket;
    private BufferedInputStream in;
    private BufferedOutputStream out;

    private DouyuConnection(){}

    static DouyuConnection initConnection(DouyuAddress douyuAddress) throws IOException {
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

    void write(DouyuMessage douyuMessage) throws IOException {
        //处理header
        byte[] messages = DouyuMessageProtocol.encode(douyuMessage);
        if (isClosed() || socket.isOutputShutdown()) {
            LOGGER.warn("connect is closed");
            return;
        }
        out.write(messages);
        out.flush();
    }

    /**
     *
     * @throws IOException IOException
     */
    Map<String,Object> read() throws IOException{
        byte[] header = decode(12);
        //获取本条消息总长度
        int totalLength = LHUtil.lowerToInt(header);
        //重复本次消息长度
        int reTotalLength = LHUtil.lowerToInt(header, 4);
        short msgType = LHUtil.lowerToShort(header, 8);
        //消息校验
        short MSG_TYPE = 690;
        if (totalLength != reTotalLength || msgType != MSG_TYPE) {
            LOGGER.error("获取消息错误,{}", totalLength);
            throw new DouyuMessageReadException("读取消息错误");
        }
        int messageLength = totalLength-8;
        byte[] msg = decode(messageLength);
        return STTDouyuMessage.deserialize(msg);
    }

    private byte[] decode(int len) throws IOException {
        int offset = 0;
        byte[] dst = new byte[len];
        while (offset < len) {
            int size = in.read(dst, offset, len - offset);
            if (size == -1) {
                throw new DouyuMessageReadException("connect is closed");
            }
            offset += size;
        }
        return dst;
    }

    void close() {
        if (!isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean isClosed(){
        return socket.isClosed();
    }

}
