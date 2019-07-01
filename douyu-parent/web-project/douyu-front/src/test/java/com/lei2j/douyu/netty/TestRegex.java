package com.lei2j.douyu.netty;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lei2j on 2018/12/16.
 */
public class TestRegex {

    @Test
    public void test4(){
        Matcher matcher = Pattern.compile("^(.(?!(\\.)\\2))+$").matcher("abc2.3.1$%^^%.&*((64");
        System.out.println(matcher.find());
    }

    @Test
    public void test5(){
        Matcher matcher = Pattern.compile("^\\w((?!(\\.)\\2)[\\w]){0,62}\\w@((?!(\\.)\\4)[\\w]){3,253}$").matcher("d.a.s@p.om");
        System.out.println(matcher.find());
    }
}
