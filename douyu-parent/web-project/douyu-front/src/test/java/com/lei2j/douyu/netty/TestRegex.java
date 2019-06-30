package com.lei2j.douyu.netty;

import org.junit.Test;

import java.io.*;
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
        Matcher matcher = Pattern.compile("^\\w((?!(\\.)\\2)[\\w\\.]){0,62}\\w@((?!(\\.)\\4)[\\w\\.]){3,253}$").matcher("d.a.s@p.om");
        System.out.println(matcher.find());
    }

    @Test
    public void test6() throws Exception {
        deepPath(new File("/opt/prod"),1);
    }

    private void deepPath(File file,int deep) throws Exception {
        System.out.println("deep:"+deep);
        if (deep > 3) {
            throw new Exception("File hierarchy greater than 3");
        }
        if(file.exists()){
            if(file.isDirectory()){
                deep++;
                File[] files = file.listFiles();
                for (File fileItem :
                        files) {
                    if(fileItem.isFile()){
                        readFile(fileItem);
                    }else {
                        deepPath(fileItem,deep);
                    }
                }
            }else {
                readFile(file);
            }
        }
    }

    private void readFile(File file){
        System.out.println(file.getName());
    }

}
