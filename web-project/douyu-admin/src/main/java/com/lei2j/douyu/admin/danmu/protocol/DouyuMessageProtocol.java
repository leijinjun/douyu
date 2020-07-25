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

package com.lei2j.douyu.admin.danmu.protocol;

import com.lei2j.douyu.admin.danmu.message.DouyuMessage;
import com.lei2j.douyu.admin.danmu.transport.DouyuTransport;
import com.lei2j.douyu.admin.danmu.exception.DouyuMessageReadException;
import com.lei2j.douyu.util.LHUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author lei2j
 * Created by lei2j on 2018/5/27.
 */
public class DouyuMessageProtocol {

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

    /**
     * 服务器返回消息标志
     */
    private static final int RECEIVE_MESSAGE_TYPE = 690;

    /**
     * 将斗鱼消息转换为指定的协议格式数据
     * @param douyuMessage 斗鱼消息
     * @return 转换后的指定协议数据
     * @throws IOException 转换错误
     */
    public static byte[] encode(DouyuMessage douyuMessage) throws IOException {
        byte[] contents = douyuMessage.getData().getBytes(Charset.forName("utf-8"));
        //获取消息长度+消息结束标识长度
        int dataLen = contents.length + 1;
        int size = length(dataLen);
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

    /**
     *  根据斗鱼协议读取指定数据字节数（不包含头部数据）
     * @param readBuf 数据存储Buffer
     * @param channel 客户端Channel
     * @return Douyu协议的数据字节数
     * @throws IOException 读取数据失败
     */
    public static byte[] decode(ByteBuffer readBuf, SocketChannel channel) throws IOException {
        byte[] header = new byte[12];
        DouyuTransport.read(header, readBuf, channel);
        int pos = 0;
        //获取本条消息总长度
        int totalLength = LHUtil.lowerToInt(header, pos);
        //重复获取本条消息总长度
        pos += 4;
        int checkTotalLength = LHUtil.lowerToInt(header, pos);
        //应答消息类型，服务器发送给客户端
        pos += 4;
        int msgType = LHUtil.lowerToShort(header, pos);
        //校验消息头
        if (totalLength != checkTotalLength || msgType != RECEIVE_MESSAGE_TYPE) {
            throw new DouyuMessageReadException("serialization error");
        }
//		获取数据长度
        int dataLength = totalLength - 8;
        byte[] data = new byte[dataLength];
        DouyuTransport.read(data, readBuf, channel);
        return data;
    }

    /**
     * 获取消息总长度
     * @param dataLength 数据长度
     */
    private static int length(int dataLength){
        return dataLength + 8;
    }
}
