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


/**
 * Imports "Fair Market Values" CSV files.
 */
public interface FmvService {

  /**
   * @return  List
   */
  List getStagingErrors();

  /**
   * @param  connection       Connection
   * @param  importVersionId  Integer
   * @param  csvFile          File
   * @param  userId           String
   */
  void importCSV(final Connection connection, final Integer importVersionId,
    final File csvFile, final String userId);

  /**
   * @param  connection Connection
   * @param  importVersionId Integer
   * @param  userId String
   */
  void processImport(final Connection connection, final Integer importVersionId,
    final String userId);

}
