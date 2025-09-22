/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 * 
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.domain.staging.EnrolmentStaging;

/**
 * @author awilkinson
 * @created Dec 3, 2010
 */
public final class EnrolmentUtils {

  /**
   * private constructor
   */
  private EnrolmentUtils() {
  }

  /**
   * @return  the current "Program Year" for last year's tax returns.
   */
  public static Integer getCurrentEnrolmentYear() {
    Calendar dateCal = Calendar.getInstance();

    int currentYear = dateCal.get(Calendar.YEAR);

    return new Integer(currentYear);
  }

  /**
   * @return the earliest year for which enrolments can be generated
   */
  public static Integer getEnrolmentStartYear() {
    return getYearConfig(ConfigurationKeys.ENROLMENT_START_YEAR);
  }
  
  /**
   * @return the number of years past the current year for which enrolments can be generated
   */
  public static Integer getEnrolmentYearsAhead() {
    return getYearConfig(ConfigurationKeys.ENROLMENT_YEARS_AHEAD);
  }

  private static Integer getYearConfig(String key) {
    ConfigurationUtility config = ConfigurationUtility.getInstance();
    String result = config.getValue(key);
    
    return Integer.valueOf(result);
  }
  
  public static boolean hasErrors(List<EnrolmentStaging> calculatedEnrolments) {
    boolean errors = false;
    for(EnrolmentStaging e : calculatedEnrolments) {
      if(e.getIsError().booleanValue()) {
        errors = true;
        break;
      }
    }
    return errors;
  }
  
  public static boolean hasFailedToGenerate(List<EnrolmentStaging> calculatedEnrolments) {
    boolean errors = false;
    for(EnrolmentStaging e : calculatedEnrolments) {
      if(e.getFailedToGenerate().booleanValue()) {
        errors = true;
        break;
      }
    }
    return errors;
  }


  public static File createEnrolmentFile(String pinsString, Integer enrolmentYear, boolean createTaskInBarn)
      throws IOException, FileNotFoundException {
    File pTempDir = IOUtils.getTempDir(); 

    File outFile = File.createTempFile("farm_enrolment_", ".csv", pTempDir);
    try (FileOutputStream fos = new FileOutputStream(outFile);
         PrintWriter pw = new PrintWriter(fos);) {
      
      pw.println(enrolmentYear.toString());
      pw.println(Boolean.valueOf(createTaskInBarn).toString());
      pw.println(pinsString);
      pw.flush();
      pw.close();
    }
    
    return outFile;
  }

}
