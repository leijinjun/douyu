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

import com.lei2j.douyu.admin.danmu.message.DouyuMessage;
import com.lei2j.douyu.admin.danmu.protocol.DouyuMessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

/**
 * @author leijinjun
 * @version v1.0
 * @date 2020/11/10
 **/
public class MessageAssemblyHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (msg instanceof DouyuMessage) {
            DouyuMessage douyuMessage = (DouyuMessage) msg;
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(DouyuMessageProtocol.MSG_SEND_TYPE);
            buffer.writeBytes(DouyuMessageProtocol.UN_USED);
            buffer.writeCharSequence(douyuMessage.getData(), CharsetUtil.UTF_8);
            buffer.writeByte(DouyuMessageProtocol.END_FLAG);
            ctx.write(buffer, promise);
        } else {
            ctx.write(msg, promise);
        }
    }
}
