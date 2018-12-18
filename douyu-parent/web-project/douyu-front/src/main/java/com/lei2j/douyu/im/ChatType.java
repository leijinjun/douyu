package com.lei2j.douyu.im;

/**
 * @author lei2j
 */
public enum ChatType {

    /**
     * 登录
     */
    LOGIN((byte) 1,"login"),
    /**
     * 心跳
     */
    KEEPLIVE((byte) 2,"keeplive");

    private byte code;

    private String type;

    ChatType(byte code,String type) {
        this.code = code;
        this.type = type;
    }

    public byte getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
