package open.microservice.accountmanagement.util;

import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class DateUtil {
    public static final String APP_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String convertDate(String originalDate, String originalFormat, String targetFormat) {
        try {
            log.debug("Converting {} with {} to {}", originalDate, originalFormat, targetFormat);
            SimpleDateFormat originalFormatter = new SimpleDateFormat(originalFormat);
            SimpleDateFormat targetFormatter = new SimpleDateFormat(targetFormat);
            Date date = originalFormatter.parse(originalDate);

            return targetFormatter.format(date);
        } catch (Exception e) {
            log.error("Error converting date: {}", e.getMessage());
            return null;
        }
    }

    public static Date stringToDate(String dateString, String formatIn) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(formatIn);
        return inputFormat.parse(dateString);
    }

    public static String dateToString(String dateString, String formatIn, String formatOut) {
        return convertDate(dateString, formatIn, formatOut);
    }
}
