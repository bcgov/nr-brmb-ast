/**
 *
 * Copyright (c) 2006, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Performs the business functions related to data exports for the feds.
 * 
 */
public interface ExportService {
  
  String FILE_01 = "01";
  String FILE_02 = "02";
  String FILE_03 = "03";
  String FILE_04 = "04";
  String FILE_05 = "05";
  String FILE_07 = "07";
  String FILE_08 = "08";
  String FILE_09 = "09";
  String FILE_20 = "20";
  String FILE_21 = "21";
  String FILE_25 = "25";
  String FILE_26 = "26";
  String FILE_30 = "30";
  String FILE_31 = "31";
  String FILE_40 = "40";
  String FILE_99 = "99";
  String FILE_60 = "60";

  File exportNumberedFilesZip(Integer programYear, String exportType, String userAccountName) throws IOException, SQLException;
  
  /**
   * Exports all verified records for the specified year
   */
  File exportDetailedScenarioExtract(Integer programYear, File tempdir) throws IOException, SQLException;

}
