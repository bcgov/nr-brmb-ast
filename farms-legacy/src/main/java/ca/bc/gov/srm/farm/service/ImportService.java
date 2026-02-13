/**
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Date;
import java.util.List;

import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageResults;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportResults;
import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;
import ca.bc.gov.srm.farm.ui.domain.dataimport.StagingResults;


/**
 * ImportService.
 */
public interface ImportService {
  
  public static final String JOB_TYPE_IMPORT = "IMPORT";
  public static final String JOB_TYPE_TRIAGE = "TRIAGE";

  /**
   * searchImports. Used by screen 250.
   * @param   importTypes importTypes
   * @return  List<ImportSearchResult>
   *
   * @throws  ServiceException  On exception.
   */
  List<ImportSearchResult> searchImports(final List<String> importTypes) throws ServiceException;

  List<ImportSearchResult> searchImports(List<String> importTypes, Date createdAfterDate) throws ServiceException;

  /**
   * Only one "in progress" import is allowed at one time.
   *
   * @param   searchResults  results from searchImports method.
   * @param   importType     importType
   * @return  true if the user should be allowed to start a new import.
   */
  boolean getAllowNewImport(final List<ImportSearchResult> searchResults, final String importType);


  /**
   * Create a new entry in the FARM_IMPORT_VERSIONS table.
   *
   * @param   importClassCode     importClassCode
   * @param   description         description
   * @param   uploadFileName      uploadFileName
   * @param   inputStream         inputStream
   * @param   userId              userId
   *
   * @return  New Import Version entry
   *
   * @throws  ServiceException  on exception
   */
  ImportVersion createImportVersion(
  	final String importClassCode,
  	final String importStateCode,
  	final String description,
    final String uploadFileName, 
    final InputStream inputStream, 
    final String userId) throws ServiceException;
  
  
  /**
   * @param   connection connection
   * @param   importClassCode importClassCode
   * @param   description description
   * @param   fileName fileName
   * @param   inputStream inputStream
   * @param   userId userId
   *
   * @return  New Import Version ID
   *
   * @throws  ServiceException  on exception
   */
  ImportVersion createImportVersion(
      final Connection connection,
      final String importClassCode,
      final String importStateCode,
      final String description,
      final String fileName, 
      final InputStream inputStream,
      final String userId)
  throws ServiceException;
  

  /**
   * @param   importVersionId  id to use
   *
   * @return  the staging details
   *
   * @throws  ServiceException  on exception
   */
  StagingResults getStagingResults(final Integer importVersionId)
    throws ServiceException;

  /**
   * The details of what happened moving the data from the staging tables to the
   * operational tables.
   *
   * @param   importVersionId  id to use
   *
   * @return  the import details
   *
   * @throws  ServiceException  on exception
   */
  ImportResults getImportResults(final Integer importVersionId)
    throws ServiceException;


  /**
   * @param   importVersionId  id to use
   *
   * @return  the ImportVersion
   *
   * @throws  ServiceException  on exception
   */
  ImportVersion getImportVersion(final Integer importVersionId)
    throws ServiceException;

  /**
   * Set the state to "Canceled". Also clears out the staging tables.
   *
   * @param   importVersionId  importVersionId
   *
   * @throws  ServiceException  on exception
   */
  void cancelImport(final Integer importVersionId) throws ServiceException;

  /**
   * Set the state to "Scheduled for Import".
   *
   * @param   importVersionId  importVersionId
   *
   * @throws  ServiceException  on exception
   */
  void confirmImport(final Integer importVersionId) throws ServiceException;
  
  /**
   * Set the state to "Scheduled for Import".
   *
   * @param   importVersionId   importVersionId
   * @param   connection        connection
   *
   * @throws  ServiceException  on exception
   */
  void confirmImport(final Integer importVersionId, final Connection connection) throws ServiceException;
  
  /**
   * Set the state to "Scheduled for Staging". Clear the audit info.
   *
   * @param   importVersionId  importVersionId
   *
   * @throws  ServiceException  on exception
   */
  void retryStaging(final Integer importVersionId) throws ServiceException;

  /**
   * See if we have to load the staging tables, or move the staging data into
   * the operational tables.
   *
   * @param   connection  must be a bare SQL connection
   *
   * @throws  ServiceException  on exception
   */
  void checkForScheduledImport(final Connection connection)
    throws ServiceException;
  
  void checkForScheduledJob(final Connection webadeConnection, final String jobType) throws ServiceException;


  /**
   * See if an imports are stuck in an "in progress" state upon startup.
   *
   * @param   connection  must be a bare SQL connection
   *
   * @throws  ServiceException  on exception
   */
  void checkForInProgessImports(final Connection connection)
    throws ServiceException;

  /**
   * The details of Benefit Triage data from the import versions table.
   *
   * @param importVersionId id to use
   *
   * @return the Benefit Triage results
   *
   * @throws ServiceException on exception
   */
  BenefitTriageResults getTriageResults(ImportVersion importVersion) throws ServiceException;
  
}
