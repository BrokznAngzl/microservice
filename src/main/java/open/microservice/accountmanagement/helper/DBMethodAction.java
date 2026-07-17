package open.microservice.accountmanagement.helper;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.ObjectUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class DBMethodAction {
    public String convertDate(String[] dateVals) {
        try {
            String originalDate = dateVals[0];
            String originalFormat = dateVals[1];
            String targetFormat = dateVals[2];

            return DateUtil.convertDate(originalDate, originalFormat, targetFormat);
        } catch (Exception e) {
            log.error("invalid arguments: {}", e.getMessage());
            return null;
        }
    }

    public String concat(Object[] values) {
        try {
            if (ObjectUtil.isEmpty(values)) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (Object value : values) {
                sb.append(value.toString());
            }

            return sb.toString();
        } catch (Exception e) {
            log.error("error concatenating values: {}", e.getMessage());
            return null;
        }
    }

    public String getCurrentDate(String[] format) {
        try {
            String dateFormat = format.length > 0 ? format[0] : "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(new Date());
        } catch (Exception e) {
            log.error("error getting current date: {}", e.getMessage());
            return null;
        }
    }

    public String isPresent(Object parameterName) {
        try {
            if (ObjectUtil.isEmpty(parameterName)) {
                return "false";
            }

            return "true";
        } catch (Exception e) {
            log.error("isPresent error: {}", e.getMessage());
            return null;
        }
    }
}
