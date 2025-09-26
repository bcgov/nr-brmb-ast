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


/**
 * Manages the processes and state transitions for the import processes.
 * Instances of this interface will invoke the required functionality and alter
 * the states.
 *
 * @author  dzwiers
 */
public interface ImportController {

  /**
   * Imports the archive into the staging area after creating the appropriate
   * state records.
   *
   * @param  importVersionId  Integer
   * @param  archive          File
   * @param  userId           userId
   */
  void importCSV(final Integer importVersionId, final File archive, final String userId);

  /**
   * Initializes the import from staging to the operational database process for
   * the version specified. This process includes state changes.
   *
   * @param  importVersionId  Integer
   * @param  userId           userId
   */
  void processImport(final Integer importVersionId, final String userId);
}
