package com.lei2j.douyu.web.controller.exception;

import com.lei2j.douyu.web.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Set;

/**
 * Created by leijinjun on 2018/12/19.
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 处理Controller异常
     * @param webRequest webRequest
     * @param request request
     * @param ex ex
     * @return Response
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Response handleControllerException(WebRequest webRequest, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex){
        if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violationSet = violationException.getConstraintViolations();
            String errorMessage = "";
            if (!CollectionUtils.isEmpty(violationSet)) {
                Optional<ConstraintViolation<?>> violationOptional = violationSet.stream().findFirst();
                errorMessage = violationOptional.isPresent() ? violationOptional.get().getMessage() : "";
            }
            printException(ex);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return new Response(400, errorMessage);
        } else if (ex instanceof MissingServletRequestParameterException) {
            printException(ex);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return Response.BAD_REQUEST;
        }else if (ex instanceof BindException) {
            printException(ex);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return Response.BAD_REQUEST;
        } else {
            printException(ex);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return Response.INTERNAL_SERVER_ERROR;
        }
    }

    private void printException(Exception ex){
        LOGGER.error("Controller exception error message:", ex);
    }
}