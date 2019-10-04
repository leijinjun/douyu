package com.lei2j.douyu.im;

/**
 * @author lei2j
 */
public enum ChatType {

    /**
     * 登录
     */
    LOGIN((byte) 1),
    /**
     * 心跳
     */
    KEEPALIVE((byte) 2);

    private byte code;


    ChatType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
