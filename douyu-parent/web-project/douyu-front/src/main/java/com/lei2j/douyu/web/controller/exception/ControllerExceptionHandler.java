package com.lei2j.douyu.web.controller.exception;

import com.lei2j.douyu.web.response.Response;
import com.lei2j.douyu.web.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by leijinjun on 2018/12/19.
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConstraintViolationException.class);

    /**
     * 处理Controller异常
     * @param webRequest
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Response handleControllerException(WebRequest webRequest, HttpServletRequest request, Exception ex){
        if (webRequest instanceof ServletWebRequest) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
        }
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violationSet = violationException.getConstraintViolations();
            String errorMessage = "";
            if (!CollectionUtils.isEmpty(violationSet)) {
                Optional<ConstraintViolation<?>> violationOptional = violationSet.stream().findFirst();
                errorMessage = violationOptional.isPresent() ? violationOptional.get().getMessage() : "";
            }
            return handleExceptionInternal(HttpStatus.OK, new Response(400, errorMessage), ex);
        } else if (ex instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException requestParameterException = (MissingServletRequestParameterException) ex;
            String errorMessage = requestParameterException.getMessage();
            return handleExceptionInternal(HttpStatus.OK, new Response(400, errorMessage), ex);
        } else if (ex instanceof BindException) {
            BindException bindException = (BindException) ex;
            FieldError fieldError = bindException.getFieldError();
            String errorMessage = fieldError.getDefaultMessage();
            return handleExceptionInternal(HttpStatus.OK, new Response(400, errorMessage), ex);
        } else {
            return handleExceptionInternal(HttpStatus.INTERNAL_SERVER_ERROR, Response.INTERNAL_SERVER_ERROR, ex);
        }
    }

    private Response handleExceptionInternal(HttpStatus status,Response response,Exception ex){
        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            LOGGER.error("Controller exception error message:", ex);
        }
        return response;
    }
}
