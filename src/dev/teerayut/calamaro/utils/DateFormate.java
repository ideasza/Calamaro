package dev.teerayut.calamaro.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormate {

	private String format = "dd/MM/yyy HH:mm:ss";
	private String formatDB = "yyy-MM-dd HH:mm:ss";
	private String formatTime = "HH:mm:ss";
	private String formatBill = "yyyMMdd";
	private String formatDate = "yyyy-MM-dd";
	
	public String getDateWithTime() {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format, new Locale("TH"));
			Date date = new Date(); 
			return dateFormat.format(date).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getDate() {
		try {
			DateFormat dateFormat = new SimpleDateFormat(formatDB, new Locale("TH"));
			Date date = new Date(); 
			return dateFormat.format(date).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getDateOnly() {
		try {
			DateFormat dateFormat = new SimpleDateFormat(formatDate, new Locale("TH"));
			Date date = new Date(); 
			return dateFormat.format(date).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Date getDateForDB() {
		try {
			java.util.Date today = new java.util.Date();
			return new java.sql.Date(today.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getTime() {
		try {
			DateFormat dateFormat = new SimpleDateFormat(formatTime, new Locale("TH"));
			Date date = new Date(); 
			return dateFormat.format(date).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getDateForBill() {
		try {
			DateFormat dateFormat = new SimpleDateFormat(formatBill, new Locale("TH"));
			Date date = new Date(); 
			return dateFormat.format(date).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
