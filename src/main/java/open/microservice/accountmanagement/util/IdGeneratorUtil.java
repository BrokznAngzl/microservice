package open.microservice.accountmanagement.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import static open.microservice.accountmanagement.util.DateUtil.YYYYMMDDHHMMSS;

public class IdGeneratorUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);

    public static String generateId(int origin, int bound) {
        String datetime = LocalDateTime.now().format(FORMATTER);
        int random = ThreadLocalRandom.current().nextInt(origin, bound);
        return datetime + "-" + random;
    }
}
