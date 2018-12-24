package com.lei2j.douyu.web.response;

/**
 * Created by lei2j on 2018/5/17.
 */
public class Response {

    public static final Response BAD_REQUEST = new Response(ResponseCode.BAD_REQUEST);
    public static final Response UNAUTHENTICATED = new Response(ResponseCode.UNAUTHENTICATED);
    public static final Response  FORBIDDEN = new Response(ResponseCode.FORBIDDEN);
    public static final Response NOT_FOUND = new Response(ResponseCode.NOT_FOUND);
    public static final Response SERVER_INTERNAL_ERROR = new Response(ResponseCode.SERVER_INTERNAL_ERROR);

    private int code;

    private String message;

    private Object body;

    public Response(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public Response(int responseCode,String message) {
        this.code = responseCode;
        this.message = message;
    }

    public Response(int responseCode, Object body) {
        this(responseCode,null);
        this.body = body;
    }

    public static Response newInstance(int code){
        return new Response(code,null);
    }

    public static Response newInstance(ResponseCode responseCode){
        return new Response(responseCode);
    }

    public Response code(int code){
         this.code = code;
         return this;
    }

    public Response text(String message){
        this.message = message;
        return this;
    }

    public Response entity(Object body){
        this.body = body;
        return this;
    }

    public static Response ok(){
        return Response.newInstance(ResponseCode.OK);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getBody() {
        return body;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Response{");
        sb.append("code=").append(code);
        sb.append(", message='").append(message).append('\'');
        sb.append(", body=").append(body);
        sb.append('}');
        return sb.toString();
    }
}
