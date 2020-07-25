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

package com.lei2j.douyu.admin.danmu.transport;

import com.lei2j.douyu.admin.danmu.exception.DouyuMessageReadException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * author: 98611
 * date: 2019/10/6
 */

public class DouyuTransport {

    public static void write(byte[] message, ByteBuffer writeBuf, SocketChannel socketChannel) throws IOException {
        Objects.requireNonNull(message);
        int messageLength = message.length;
        writeBuf.clear();
        for (int offset = 0; offset < messageLength; ) {
            int remaining = writeBuf.remaining();
            int len = messageLength - offset;
            len = len > remaining ? remaining : len;
            writeBuf.put(message, offset, len);
            offset += len;
            //转换为读模式
            writeBuf.flip();
            socketChannel.write(writeBuf);
            writeBuf.clear();
        }
    }

    public static void read(byte[] dst, ByteBuffer readBuf, SocketChannel socketChannel) throws IOException {
        Objects.requireNonNull(dst);
        int size = dst.length;
        readBuf.clear();
        for (int offset = 0; offset < size; ) {
            int remaining = readBuf.remaining();
            int limit = size - offset;
            limit = limit > remaining ? remaining : limit;
            readBuf.limit(limit);
            int len = socketChannel.read(readBuf);
            if (len == 0) {continue;}
            if (len == -1) {
                throw new DouyuMessageReadException("server connection is closed");
            }
            readBuf.flip();
            readBuf.get(dst, offset, len);
            readBuf.clear();
            offset += len;
        }
    }
}
