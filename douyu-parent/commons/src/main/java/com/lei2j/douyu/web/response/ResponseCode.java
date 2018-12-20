package com.lei2j.douyu.web.response;

/**
 * Created by lei2j on 2018/5/17.
 */
public enum  ResponseCode {

    OK(200,"成功"),
    BAD_REQUEST(400,"请求出错"),
    UNAUTHORIZED(401,"未经授权"),
    FORBIDDEN(403,"权限不足"),
    NOT_FOUND(404,"资源未找到"),
    SERVER_INTERNAL_ERROR(500,"服务器内部错误"),

    ROOM_CONNECT_EXISTS(50,"房间连接已存在"),
    ROOM_CONNECT_ERROR(51,"房间连接失败");

    ResponseCode(int code) {
        this.code = code;
    }

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseCode codeOf(int code){
        ResponseCode[] values = ResponseCode.values();
        for (ResponseCode var:
             values) {
            if(var.getCode()==code){
                return var;
            }
        }
        return null;
    }

}
