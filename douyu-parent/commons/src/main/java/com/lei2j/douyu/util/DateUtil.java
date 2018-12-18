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
