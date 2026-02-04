package ca.bc.gov.srm.farm.util;

import java.util.Calendar;
import java.util.Date;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;


/**
 * ProgramYearUtils.
 */
public final class ProgramYearUtils {

  /** private constructor */
  private ProgramYearUtils() {

  }

  /**
   * @return  the current "Program Year" for last year's tax returns.
   */
  public static Integer getCurrentProgramYear() {
    int currentYear = getCurrentCalendarYear();

    return new Integer(currentYear - 1);
  }

  public static Integer getCurrentCalendarYear() {
    Calendar dateCal = Calendar.getInstance();
    dateCal.setTime(new Date());

    int currentYear = dateCal.get(Calendar.YEAR);
    return currentYear;
  }

  public static int getLatestAdminYear() {
    return getCurrentCalendarYear() + 1;
  }

  /**
   * @return the year that BC first administrated the AgriStability program
   */
  public static Integer getFirstAdminYear() {
    ConfigurationUtility config = ConfigurationUtility.getInstance();
    String key = ConfigurationKeys.FIRST_BC_ADMIN_YEAR;
    String result = config.getValue(key);
    
    return Integer.valueOf(result);
  }

  /**
   * @return array of years that BC has administrated the AgriStability program
   */
  public static int[] getAdminYears() {
    int firstYear = getFirstAdminYear();
    int latestProgramYear = getLatestAdminYear();
    int numYears = latestProgramYear - firstYear + 1;
    int[] adminYears = new int[numYears];
    int yi = 0;
    for(int year = latestProgramYear; year >= firstYear; year--) {
      adminYears[yi++] = year;
    }
    return adminYears;
  }

}
