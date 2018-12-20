package com.lei2j.douyu.web.controller;

import com.lei2j.douyu.core.constant.ValidatorConstants;
import com.lei2j.douyu.core.controller.BaseController;
import com.lei2j.douyu.service.UserService;
import com.lei2j.douyu.web.response.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by lei2j on 2018/12/16.
 */
@RestController
@RequestMapping("/auth/user")
@Validated
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Response register(@RequestParam("email")@Pattern(regexp= ValidatorConstants.REGEX_EMAIL,message="邮箱格式不正确") String email,
                             @RequestParam("captcha")@Pattern(regexp = ValidatorConstants.REGEX_CAPTCHA,message = "验证码格式不正确") String captcha,
                             @RequestParam("password")@Pattern(regexp = ValidatorConstants.REGEX_PASSWORD,message = "密码不符合要求") String password){
        return Response.ok();
    }

    @PostMapping("/login")
    public Response login(@RequestParam("username")@NotBlank String username,
                          @RequestParam("password")@NotBlank String password){
        return Response.ok();
    }
}
