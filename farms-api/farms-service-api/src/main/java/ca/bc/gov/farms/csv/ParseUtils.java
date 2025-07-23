package ca.bc.gov.farms.csv;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ParseUtils {

    private ParseUtils() {
    }

    private static Logger logger = LoggerFactory.getLogger(ParseUtils.class);

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static Date parseDate(final String s) {
        try {
            if (StringUtils.isNotBlank(s)) {
                return DATE_FORMAT.parse(s.trim());
            }
        } catch (ParseException e) {
            logger.error("Unexpected error: ", e);
        }
        return null;
    }

    public static Integer parseInteger(final String s) {
        try {
            if (StringUtils.isNotBlank(s)) {
                return Integer.valueOf(s.trim());
            }
        } catch (NumberFormatException e) {
            logger.error("Unexpected error: ", e);
        }
        return null;
    }

    public static Float parseFloat(final String s) {
        try {
            if (StringUtils.isNotBlank(s)) {
                return Float.valueOf(s.trim());
            }
        } catch (NumberFormatException e) {
            logger.error("Unexpected error: ", e);
        }
        return null;
    }

    public static Double parseDouble(final String s) {
        try {
            if (StringUtils.isNotBlank(s)) {
                return Double.valueOf(s.trim());
            }
        } catch (NumberFormatException e) {
            logger.error("Unexpected error: ", e);
        }
        return null;
    }

    public static BigDecimal parseBigDecimal(final String s) {
        try {
            if (StringUtils.isNotBlank(s)) {
                return new BigDecimal(s.trim());
            }
        } catch (NumberFormatException e) {
            logger.error("Unexpected error: ", e);
        }
        return null;
    }

    public static Boolean parseBoolean(final String s) {
        if (StringUtils.isNotBlank(s)) {
            String t = s.trim();
            return "Y".equalsIgnoreCase(t) || "T".equalsIgnoreCase(t) || "TRUE".equalsIgnoreCase(t);
        }
        return Boolean.FALSE;
    }
}
