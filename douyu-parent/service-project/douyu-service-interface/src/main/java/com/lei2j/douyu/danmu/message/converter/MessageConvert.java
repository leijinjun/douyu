package com.lei2j.douyu.danmu.message.converter;

import com.lei2j.douyu.danmu.pojo.DouyuMessage;
import com.lei2j.douyu.util.LHUtil;

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
    private static final byte END_FLAG = '\0';

    /**
     * 消息发送类型
     */
    private static final byte[] MSG_SEND_TYPE = LHUtil.toLowerShort((short) 689);

    /**
     * 未使用字段
     */
    private static final byte[] UN_USED = new byte[2];

    public static byte[] preConvert(DouyuMessage douyuMessage) throws IOException {
        byte[] contents = douyuMessage.getData().getBytes(Charset.forName("utf-8"));
        //获取消息长度+消息结束标识长度
        int size = length(contents.length + 1);
        //消息总长度转换为小端整数
        byte[] lens = LHUtil.toLowerInt(size);
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream((lens.length << 1) + size)) {
            stream.write(lens);
            stream.write(lens);
            stream.write(MSG_SEND_TYPE);
            stream.write(UN_USED);
            stream.write(contents);
            stream.write(END_FLAG);
            return stream.toByteArray();
        }
    }

    public static DouyuMessage postConvert(byte[] message) {
        DouyuMessage douyuMessage = new DouyuMessage();
        String var1 = new String(message, Charset.forName("utf-8"));
        douyuMessage.setResult(var1);
        return douyuMessage;
    }

    /**
     * 获取消息总长度
     * @param dataLength 数据长度
     */
    private static int length(int dataLength){
        return dataLength + 8;
    }
}
