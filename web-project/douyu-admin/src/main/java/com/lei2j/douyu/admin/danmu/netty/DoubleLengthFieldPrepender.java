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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.ByteOrder;
import java.util.List;

/**
 * @author leijinjun
 * @version v1.0
 * @date 2020/11/10
 **/
public class DoubleLengthFieldPrepender extends LengthFieldPrepender {

    public DoubleLengthFieldPrepender(int lengthFieldLength) {
        super(ByteOrder.LITTLE_ENDIAN, lengthFieldLength, 4, false);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
        Object o = out.get(0);
        if (o instanceof ByteBuf) {
            ByteBuf lenBuf = (ByteBuf) o;
            out.add(1, lenBuf.duplicate().retain());
        }
    }
}
