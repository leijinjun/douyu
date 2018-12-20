package com.lei2j.douyu.web.config;

import com.lei2j.douyu.web.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
import java.util.Optional;
import java.util.Set;

/**
 * Created by leijinjun on 2018/12/19.
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

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
        if(ex instanceof ConstraintViolationException){
            final Response response = Response.BAD_REQUEST;
            ConstraintViolationException violationException = (ConstraintViolationException) ex;
            Set<ConstraintViolation<?>> violationSet = violationException.getConstraintViolations();
            if(!CollectionUtils.isEmpty(violationSet)){
                Optional<ConstraintViolation<?>> violationOptional = violationSet.stream().findFirst();
                violationOptional.ifPresent((var)->response.text(var.getMessage()));
            }
            return response;
        }
        logger.error("Exception handler error message:",ex);
        return Response.SERVER_INTERNAL_ERROR;
    }
}
