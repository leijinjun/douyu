package com.lei2j.douyu.core.constant;

/**
 * Created by lei2j on 2018/12/16.
 */
public interface ValidatorConstants {

    /**
     * 邮箱
     */
    String REGEX_EMAIL = "^\\w((?!(\\.)\\2)[\\w\\.]){0,62}\\w@((?!(\\.)\\4)[\\w\\.]){3,253}$";

    /**
     * 验证码
     */
    String REGEX_CAPTCHA = "^[0-9]{6}$";

    String REGEX_PASSWORD = "^(?!\\d+$)(?![a-zA-Z]+$)[\\w!@#$%^&*]{8,20}$";
}
