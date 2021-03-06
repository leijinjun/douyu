/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

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
