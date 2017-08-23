package com.oucb303.training.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
	public final static String DateToString(Date date)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(date);
	}

	public final static String DateToString(Date date, String format)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
	public static String getTimeIntToHms(int dateTime){
		Date d = new Date(dateTime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(d);
	}

	public static Date stringToDate(String dateString)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try
		{
			date = sdf.parse(dateString);
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		return date;
	}
	
	public static void main(String[] args)
	{
		System.out.println(System.currentTimeMillis());
	}
	public static int compare_date(String DATE1, String DATE2) {


		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				//   System.out.println("dt1 在dt2后");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				//  System.out.println("dt1在dt2	前");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	@SuppressLint("SimpleDateFormat")
	public static String getDate() {

		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		String date = df.format(day);
		return date;
	}
}
