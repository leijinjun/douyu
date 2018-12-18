package com.lei2j.douyu.web.response;

/**
 * Created by lei2j on 2018/5/17.
 */
public class Response {

    public static final Response BAD_REQUEST = new Response(ResponseCode.BAD_REQUEST);
    public static final Response UNAUTHORIZED = new Response(ResponseCode.UNAUTHORIZED);
    public static final Response  FORBIDDEN = new Response(ResponseCode.FORBIDDEN);
    public static final Response NOT_FOUND = new Response(ResponseCode.NOT_FOUND);
    public static final Response SERVER_INTERNAL_ERROR = new Response(ResponseCode.SERVER_INTERNAL_ERROR);

    private int code;

    private String message;

    private Object body;

    public Response() {
    }

    public Response(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public Response(ResponseCode responseCode, Object body) {
        this(responseCode);
        this.body = body;
    }

    public static Response newInstance(ResponseCode code){
        return new Response(code);
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

}
