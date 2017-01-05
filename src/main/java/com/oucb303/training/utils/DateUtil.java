package com.oucb303.training.utils;

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
}
