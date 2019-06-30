package com.lei2j.douyu.danmu.message.converter;

import com.lei2j.douyu.danmu.pojo.DouyuMessage;
import com.lei2j.douyu.util.LHUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/27.
 */
public class MessageConvert {

    /**
     * 消息结束标志
     */
    private static final char END_FLAG = '\0';
    /**
     * 消息发送类型
     */
    private static final short MSG_SEND_TYPE = 689;

    /**
     * 未使用字段
     */
    private static final byte[] UN_USED = new byte[2];

    public static byte[] preConvert(DouyuMessage douyuMessage) {
        StringBuilder data = douyuMessage.getData();
        data.append(END_FLAG);
        byte[] dataBytes = data.toString().getBytes(Charset.forName("utf-8"));
        //消息发送类型转换为小端整数
        byte[] ls = LHUtil.toLowerShort(MSG_SEND_TYPE);
        int dataLength = dataBytes.length;
        //获取消息总长度
        int size = length(dataLength);
        //消息总长度转换为小端整数
        byte[] lens = LHUtil.toLowerInt(size);
        byte[] messages = null;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream((lens.length << 1) + size)) {
            stream.write(lens);
            stream.write(lens);
            stream.write(ls);
            stream.write(UN_USED);
            stream.write(dataBytes);
            messages = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static DouyuMessage postConvert(byte[] message) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(message)) {
            DouyuMessage douyuMessage = new DouyuMessage();
            byte[] data = new byte[message.length];
            int size = in.read(data);
            if (size == -1) {
                return douyuMessage;
            }
            String var1 = new String(data, 0, size).trim();
            douyuMessage.append(var1);
            return douyuMessage;
        }
    }

    /**
     * 获取消息总长度
     * @param dataLength 数据长度
     */
    private static int length(int dataLength){
        return dataLength+8;
    }
}
