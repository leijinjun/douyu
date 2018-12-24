package com.lei2j.douyu.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by leijinjun on 2018/12/24.
 */
public class CookieUtil {

    public static String getValueByCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie:
                 cookies) {
                if (cookie.getName().equals(cookieName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
