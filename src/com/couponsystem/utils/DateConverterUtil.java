package com.couponsystem.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Tal Yamin
 *
 */

/*
 * DateConverterUtil is used as utility for converting dates in different
 * classes. This project requires conversions between date to local date, string
 * to local date, and string formats. For example Derby tables only can receive 
 * Date object and not LocalDate object. Another example is the requirement to print 
 * the date in format of Israel local date which different from default date format.
 */

public class DateConverterUtil {

	/* DateTimeFormatter in format of Israel Local Date */
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

	
	/* static object of LocalDate */
	private static LocalDate localDate;
	
	/* static object of String */
	private static String customDate;

	
	/* This static method receive String of date and convert it to LocalDate object */
	public static LocalDate convertStringDate(String endDate) {
		localDate = LocalDate.parse(endDate, formatter);
		return localDate;
	}

	/* This static method receive LocalDate and convert it to String of date */
	public static String DateStringFormat(LocalDate date) {
		customDate = date.format(formatter);
		return customDate;
	}

	/* This static method receive Date and convert it to Local Date*/
	public static LocalDate convertDateLocal(Date date) {
		customDate = date.toString();
		localDate = LocalDate.parse(customDate);
		return localDate;
	}
	
	

}
