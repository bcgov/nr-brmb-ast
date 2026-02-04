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
import java.sql.Connection;
import java.util.List;

import ca.bc.gov.srm.farm.csv.CSVParserException;

/**
 * Imports "Accrual Adjusted Reference Margin" CSV files.
 */
public interface AarmService {
	
	/**
   * Used for unit testing.
   * @return a list of CSVParserException or Strings
   */
  List<CSVParserException> getStagingErrors();

  /**
   * Imports the CSV into the staging area.
   *
   * @param  connection  connection
   * @param  importVersionId  Integer
   * @param  csvFile          File
   * @param  userId           userId
   */
  void importCSV(
  		final Connection connection,
  		final Integer importVersionId, 
  		final File csvFile, 
  		final String userId);

  /**
   * Transfars the data from the staging table to the operational table.
   * 
   *  @param  connection  connection
   * @param  importVersionId  Integer
   * @param  userId           userId
   */
  void processImport(
  		final Connection connection,
  		final Integer importVersionId, 
  		final String userId);
}
