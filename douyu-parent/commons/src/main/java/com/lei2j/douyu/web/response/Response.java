package com.lei2j.douyu.web.response;

/**
 * Created by lei2j on 2018/5/17.
 */
public class Response {

    public static final Response BAD_REQUEST = new Response(ResponseCode.BAD_REQUEST);
    public static final Response UNAUTHENTICATED = new Response(ResponseCode.UNAUTHENTICATED);
    public static final Response  FORBIDDEN = new Response(ResponseCode.FORBIDDEN);
    public static final Response NOT_FOUND = new Response(ResponseCode.NOT_FOUND);
    public static final Response INTERNAL_SERVER_ERROR = new Response(ResponseCode.INTERNAL_SERVER_ERROR);

    private int errCode;

    private String errMsg;

    private Object body;

    public Response(ResponseCode responseCode) {
        this(responseCode.getCode(),responseCode.getMessage());
    }

    public Response(int errCode,String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Response(int errCode, Object body) {
        this(errCode,null);
        this.body = body;
    }

    public static Response newInstance(int errCode){
        return new Response(errCode, (String) null);
    }

    public static Response newInstance(ResponseCode responseCode){
        return new Response(responseCode);
    }

    public Response entity(Object body){
        this.body = body;
        return this;
    }

    public Response text(String errMsg){
        this.errMsg = errMsg;
        return this;
    }

    public static Response ok(){
        return Response.newInstance(ResponseCode.OK);
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public Object getBody() {
        return body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Response{");
        sb.append("errCode=").append(errCode);
        sb.append(", errMsg='").append(errMsg).append('\'');
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
