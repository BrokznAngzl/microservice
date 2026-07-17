package open.microservice.accountmanagement.helper;


import lombok.extern.log4j.Log4j2;
import open.microservice.accountmanagement.util.DateUtil;
import open.microservice.accountmanagement.util.ObjectUtil;
import open.microservice.accountmanagement.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class DBMethodAction {
    public String convertDate(Object[] dateVals) {
        try {
            String originalDate = dateVals[0].toString();
            String originalFormat = dateVals[1].toString();
            String targetFormat = dateVals[2].toString();

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

    public String getCurrentDate(String format) {
        try {
            String dateFormat = StringUtil.isEmpty(format) ? "yyyy-MM-dd" : format;
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

    public String ping() {
        return "pong";
    }
}
