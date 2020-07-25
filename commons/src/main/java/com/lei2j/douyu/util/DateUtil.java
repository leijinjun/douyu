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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lei2j
 */
public class DateUtil {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static String localDateFormat(LocalDate localDate) {
		return localDate.format(DATE_FORMATTER);
	}
	
	public static String localDateFormat(LocalDate localDate,String pattern) {
		return localDate.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static String localDateTimeFormat(LocalDateTime localDateTime) {
		return localDateTime.format(DATE_TIME_FORMATTER);
	}
	
	public static String localDateTimeFormat(LocalDateTime localDateTime,String pattern) {
		return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static int returnIntDay(long t1,long t2){
		long divisor = 86400000L;
		Long t = Math.abs(t1-t2)/divisor;
		return t.intValue();
	}
}
