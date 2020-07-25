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

package com.lei2j.douyu.im;

import com.lei2j.douyu.util.LHUtil;

/**
 * @author lei2j
 */
public class Header {

    /**
     * 固定值,2个字节,小端整数,表明该消息为Netty协议消息
     */
    public static final short PROTOCOL_TYPE  = (short)0xABEF;

    /**
     * 主版本号,1个字节
     */
    public static final byte MAJOR_VERSION = 1;

    /**
     * 次版本号,1个字节
     */
    public static final byte MINOR_VERSION = 1;

    /**
     * 消息长度,4个字节,小端整数,包括消息头和消息体
     */
    private int length;

    /**
     * 消息类型,1个字节
     */
    private ChatType type;

    /**
     * 扩展字段,1个字节
     * 暂不使用
     */
    private byte extend = 0;

    public Header(ChatType type,int bodyLength) {
        this.type = type;
        this.length = bodyLength + 10;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length+10;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public byte getExtend() {
        return extend;
    }

    public void setExtend(byte extend) {
        this.extend = extend;
    }

    public byte[] getBytes() {
    	byte[] b = new byte[10];
    	System.arraycopy(LHUtil.toLowerShort(PROTOCOL_TYPE), 0, b, 0, 2);
    	b[2] = MAJOR_VERSION;
    	b[3] = MINOR_VERSION;
    	System.arraycopy(LHUtil.toLowerInt(length), 0, b, 4, 4);
    	b[8] = type.getCode();
    	b[9] = extend;
    	return b;
    }
}
