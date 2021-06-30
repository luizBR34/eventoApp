package com.eventoApp.mappers.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
public class DateUtils {
    
    // The date formatter
    // - dd:   day in month (number)
    // - MM:   month in year (number)
    // - yyyy: year
	
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
    
    // read a date string and parse/convert to a date
    public static Date parseDate(String dateStr) throws ParseException {
    	
    	dateStr = dateStr.replace('/', '-');
        Date theDate = formatter.parse(dateStr);
        
        return theDate;        
    }
    
    
    // read a date and format/convert to a string
    public static String formatDate(Date theDate) {
        
        String result = "";
        
        if (theDate != null) {
            result = formatter.format(theDate);
        }
        
        return result;
    }
    
    
    public static String getDate(String dateStr) throws ParseException {

    	Date date = parseDate(dateStr);
    	return dateFormat.format(date);    	
    }
    
    
    public static String getHour(String dateStr) throws ParseException {

    	Date date = parseDate(dateStr);
    	return hourFormat.format(date);    	
    }
 
}