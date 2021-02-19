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

package com.lei2j.douyu.admin.danmu.netty;

import com.lei2j.douyu.admin.danmu.exception.DouyuMessageReadException;
import com.lei2j.douyu.admin.danmu.protocol.DouyuMessageProtocol;
import com.lei2j.douyu.admin.danmu.serialization.STTDouyuMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author leijinjun
 * @version v1.0
 * @date 2020/11/11
 **/
public class MessageParseHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MessageParseHandler.class);

    private static final ByteProcessor END_PROCESSOR = new ByteProcessor.IndexOfProcessor((byte) '\0');


    public MessageParseHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
//            skip length field
            buf.skipBytes(8);
            short msgType = buf.readShortLE();
            if (msgType != DouyuMessageProtocol.RECEIVE_MESSAGE_TYPE) {
                throw new DouyuMessageReadException("message type error");
            }
//            skip unused filed
            buf.skipBytes(2);
            int index = buf.writerIndex() - 1;
            if (buf.forEachByteDesc(index, 1, END_PROCESSOR) < 0) {
                throw new DouyuMessageReadException("message end flag error");
            }
            String s = buf.toString(CharsetUtil.UTF_8);
            Map<String, Object> map = STTDouyuMessage.parse(s);
            logger.debug("消息解析：{}", map);
            buf.release();
            ctx.fireChannelRead(map);
        }else {
            super.channelRead(ctx, msg);
        }
    }

}
