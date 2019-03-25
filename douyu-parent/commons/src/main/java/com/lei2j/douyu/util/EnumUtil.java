package com.lei2j.douyu.util;

import com.lei2j.douyu.core.enums.EnumType;

/**
 * Created by leijinjun on 2019/2/15.
 */
public class EnumUtil {

    private EnumUtil(){}

    public static <T extends EnumType> T codeOf(Class<T> classType, int code) {
        if (classType.isEnum()) {
            T[] enumConstants = classType.getEnumConstants();
            for (T t :
                    enumConstants) {
                if (t.eq(code)) return t;
            }
            return null;
        }
        throw new RuntimeException("Class " + classType + " is not Enum");
    }
}
