/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 *
 * @author antonio
 */
public class DatetimeHelper {
    
    private static Date noDate = null;
    public static final String NO_DATE_STRING = "2000-01-01T00:00:00+00:00";
    
    public static Date getNoDate(){
        if (DatetimeHelper.noDate == null){
            DatetimeHelper.generateNoDate();
        }
        return noDate;
    }
    
    private static void generateNoDate(){
        TemporalAccessor ta = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(NO_DATE_STRING);
        noDate = Date.from(Instant.from(ta));
    }
    
    public static boolean isNoDate(Date date){
        return DatetimeHelper.getNoDate().compareTo(date) == 0;
    }
}
