package com.lei2j.douyu.im;

import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author lei2j
 */
public class Encoder {

    public static byte[] encoder(Message message){
    	String body = message.getBody();
    	byte[] bodyBytes = body.getBytes(Charset.forName("utf-8"));
    	int bodyLength = bodyBytes.length;
    	Header header = new Header(message.getType(),bodyLength);
    	byte[] headerBytes = header.getBytes();
    	int headerLength = headerBytes.length;
    	int totalLength = bodyLength+headerLength;
    	byte[] target = new byte[totalLength];
    	System.arraycopy(headerBytes, 0, target, 0, headerLength);
    	System.arraycopy(bodyBytes, 0, target, headerLength, bodyLength);
        return target;
    }

    public Message read(SocketChannel socketChannel){
//        byte[] ptype = new byte[2];
//        System.arraycopy(b,0,ptype,0,2);
//        if(LHUtil.lowerToShort(ptype)!=Header.PROTOCOL_TYPE){
//            return null;
//        }

        return null;
    }

}
