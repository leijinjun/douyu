package com.lei2j.douyu.service;

import com.lei2j.douyu.core.constant.ValidatorConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by lei2j on 2018/12/23.
 */
public interface EmailNotifyService {

    boolean sendEmail(@NotBlank @Pattern(regexp = ValidatorConstants.REGEX_EMAIL) String email, String content);
}
