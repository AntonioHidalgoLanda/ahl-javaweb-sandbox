/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author antonio
 */
public class DatetimeHelper {
    
    private static Date noDate = null;
    public static final String NO_DATE_STRING = "2000-01-01T00:00:00";
    
    public static Date getNoDate(){
        if (DatetimeHelper.noDate == null){
            DatetimeHelper.generateNoDate();
        }
        return noDate;
    }
    
    private static void generateNoDate(){
        DateFormat formatter = new SimpleDateFormat("ISO_OFFSET_DATE_TIME");
        try {
            DatetimeHelper.noDate = formatter.parse(DatetimeHelper.NO_DATE_STRING);
        } catch (ParseException ex) {
            System.err.println(ex);
        }
    }
    
    public static boolean isNoDate(Date date){
        return DatetimeHelper.getNoDate().compareTo(date) == 0;
    }
}
