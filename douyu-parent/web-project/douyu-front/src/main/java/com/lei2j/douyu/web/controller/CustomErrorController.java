package com.lei2j.douyu.web.controller;

import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.response.ResponseCode;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by leijinjun on 2018/12/19.
 */
@Controller
@RequestMapping("/error")
public class CustomErrorController extends AbstractErrorController {

    public CustomErrorController(){
        super(new ErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
                return null;
            }

            @Override
            public Throwable getError(WebRequest webRequest) {
                return null;
            }
        });
    }

    @ResponseBody
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Response error(HttpServletRequest request){
        HttpStatus httpStatus = getStatus(request);
        Response response = Response.newInstance(ResponseCode.codeOf(httpStatus.value()));
        if (response == null) {
            response = Response.SERVER_INTERNAL_ERROR;
        }
        return response;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
