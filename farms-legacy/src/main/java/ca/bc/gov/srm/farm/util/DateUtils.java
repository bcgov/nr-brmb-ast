package ca.bc.gov.srm.farm.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;


/**
 * Used to consistently format dates.
 */
public final class DateUtils {

  public static final String TIME_FORMAT_STR = "HH:mm";

  public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
      TIME_FORMAT_STR);
  
  public static final String YEAR_FORMAT_STR = "yyyy";
  
  public static final String NEVER_EXPIRES_DATE_STRING = "9999-12-31";

  /** constructor. */
  private DateUtils() {
  }

  /**
   * @param   date  the date to format
   *
   * @return  the formatted date
   */
  public static String formatDate(final Date date) {
    SimpleDateFormat df = new SimpleDateFormat(DataParseUtils.DATE_FORMAT);

    if (date != null) {
      return df.format(date);
    }

    return null;
  }
  
  /**
   * @param   date  the date to format
   *
   * @return  the formatted date
   */
  public static String formatDateTime(final Date date) {
    SimpleDateFormat df = new SimpleDateFormat(DataParseUtils.DATETIME_FORMAT);
    
    if (date != null) {
      return df.format(date);
    }
    
    return null;
  }
  
  /**
   * @param   date  the date to format
   *
   * @return  the formatted date
   */
  public static String formatDateForDatePicker(final Date date) {
    SimpleDateFormat df = new SimpleDateFormat(DataParseUtils.DATE_PICKER_DATE_FORMAT);
    
    if (date != null) {
      return df.format(date);
    }
    
    return null;
  }


  public static Date setTime(Date d, String timeStr) {
    Calendar t = Calendar.getInstance();

    Date timeDate = TIME_FORMAT.parse(timeStr, new ParsePosition(0));
    t.setTime(timeDate);

    return getTimestamp(d,
        t.get(Calendar.HOUR_OF_DAY),
        t.get(Calendar.MINUTE),
        t.get(Calendar.SECOND),
        t.get(Calendar.MILLISECOND));
  }


  public static Date getTimestamp(Date d, int hour, int min, int sec, int ms) {
    Calendar cal = Calendar.getInstance();

    cal.setTime(d);

    return getTimestamp(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH),
        hour,
        min,
        sec,
        ms);
  }


  public static Date getTimestamp(int year, int month, int dayOfMonth, int hour,
    int min, int sec, int ms) {
    Calendar cal = Calendar.getInstance();

    cal.set(Calendar.MILLISECOND, ms);
    cal.set(Calendar.SECOND, sec);
    cal.set(Calendar.MINUTE, min);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    cal.set(Calendar.MONTH, month);
    cal.set(Calendar.YEAR, year);

    return cal.getTime();
  }


  /**
   * @param   date   date
   * @param   start  start
   * @param   stop   stop
   *
   * @return  true if date between start and stop
   */
  public static boolean isBetween(Date date, Date start, Date stop) {
    boolean between = false;

    if ((date != null) && (start != null) && (stop != null)) {
      between = date.after(start) && date.before(stop);
    }

    return between;
  }
  
  
  /**
   * @param a Date
   * @param b Date
   * @return boolean
   */
  public static boolean equal(Date a, Date b) {
    boolean result;
    if(a == null && b == null) {
      result = true;
    } else {
      if(a != null && a.equals(b)) {
        result = true;
      } else {
        result = false;
      }
    }
    return result;
  }


  public static boolean isExpired(Date date) {
    boolean expired = false;

    if (date != null) {
      Date now = new Date();
      expired = date.before(now);
    }

    return expired;
  }


  public static boolean isEffective(Date date) {
    boolean effective = false;

    if (date != null) {
      Date now = new Date();
      effective = date.after(now);
    }

    return effective;
  }


  public static Date addYears(Date date, int years) {
    
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(date.getTime());
    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + years);

    return cal.getTime();
  }
  
  public static Date subtractYears(Date date, int years) {
    return addYears(date, 0 - years);
  }
  
  
  public static Date oneYearAgo() {
    return subtractYears(todayAtStartOfDay(), 1);
  }
  
  
  public static Date getNeverExpiresDate() {
    Date result = null;
    
    try {
      result = DataParseUtils.parseDate(NEVER_EXPIRES_DATE_STRING);
    } catch (ParseException e) {
      throw new IllegalStateException("Unexpected failure to parse NEVER_EXPIRES_DATE_STRING");
    }
    
    return result;
  }
  
  /**
   * @param   date  the date to format
   *
   * @return  the formatted year as an Integer
   */
  public static Integer getYearFromDate(final Date date) {
    SimpleDateFormat df = new SimpleDateFormat(YEAR_FORMAT_STR);

    if (date != null) {
      return  Integer.valueOf(df.format(date));
    }

    return null;
  }
  
  public static LocalDate convertToLocalDate(Date dateToConvert) {
    return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static Date convertToDate(LocalDate dateToConvert) {
    return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }
  
  public static Date todayAtStartOfDay() {
    ZoneId zoneId = ZoneId.systemDefault();
    ZonedDateTime midnightTodayZoned = LocalDate.now(zoneId).atStartOfDay(zoneId);
    Instant midnightInstant = midnightTodayZoned.toInstant();
    Date midnightDate = Date.from(midnightInstant);

    return midnightDate;
  }
}
