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
package ca.bc.gov.srm.farm.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.csv.ArchiveFormatValidator;
import ca.bc.gov.srm.farm.csv.CSVParserException;
import ca.bc.gov.srm.farm.csv.FileCacheManager;
import ca.bc.gov.srm.farm.csv.FileHandle;
import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.staging.Z01ParticipantInfo;
import ca.bc.gov.srm.farm.domain.staging.Z02PartpntFarmInfo;
import ca.bc.gov.srm.farm.domain.staging.Z03StatementInfo;
import ca.bc.gov.srm.farm.domain.staging.Z04IncomeExpsDtl;
import ca.bc.gov.srm.farm.domain.staging.Z05PartnerInfo;
import ca.bc.gov.srm.farm.domain.staging.Z21ParticipantSuppl;
import ca.bc.gov.srm.farm.domain.staging.Z22ProductionInsurance;
import ca.bc.gov.srm.farm.domain.staging.Z23LivestockProdCpct;
import ca.bc.gov.srm.farm.domain.staging.Z28ProdInsuranceRef;
import ca.bc.gov.srm.farm.domain.staging.Z29InventoryRef;
import ca.bc.gov.srm.farm.domain.staging.Z40PrtcpntRefSuplDtl;
import ca.bc.gov.srm.farm.domain.staging.Z42ParticipantRefYear;
import ca.bc.gov.srm.farm.domain.staging.Z50ParticipntBnftCalc;
import ca.bc.gov.srm.farm.domain.staging.Z51ParticipantContrib;
import ca.bc.gov.srm.farm.domain.staging.Z99ExtractFileCtl;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ImportController;
import ca.bc.gov.srm.farm.util.CacheUtils;

/**
 * Creates a copy of itself using the connection provided.
 * 
 * @author dzwiers
 */
public final class ImportControllerFactory implements ImportController {

  private Logger logger = LoggerFactory.getLogger(ImportControllerFactory.class);

  /** Connection. */
  private Connection conn = null;

  /** File. */
  private File tmpDir;

  public static final int DEFAULT_ERRORS = 250000;

  private int errorsAllowed;

  /**
   * Creates a new ImportControllerFactory object.
   * 
   * @param c Connection
   * @param pTmpDir File
   * @param errors errors
   */
  private ImportControllerFactory(final Connection c, final File pTmpDir,
      final int errors) {
    this.conn = c;
    this.tmpDir = pTmpDir;
    this.errorsAllowed = errors;
  }

  /**
   * Creates a copy of itself using the connection provided.
   * 
   * @param c
   *          Connection
   * @param pTmpDir
   *          File
   * 
   * @return ImportController
   * 
   * @see ImportController
   */
  public static ImportController getInstance(final Connection c,
      final File pTmpDir) {
    return getInstance(c, pTmpDir, DEFAULT_ERRORS);
  }

  /**
   * Creates a copy of itself using the connection provided.
   * 
   * @param c Connection
   * @param pTmpDir File
   * @param errorsAllowed errorsAllowed
   * @return ImportController
   * @see ImportController
   */
  public static ImportController getInstance(final Connection c,
      final File pTmpDir, final int errorsAllowed) {
    return new ImportControllerFactory(c, pTmpDir, errorsAllowed);
  }

  /**
   * Imports the archive into the staging area after creating the appropriate
   * state records.
   * 
   * @param importVersionId Integer
   * @param archive File
   * @param userId userId
   * @see ImportController#importCSV(Integer, java.io.File, String)
   */
  @Override
  public void importCSV(final Integer importVersionId, final File archive,final String userId) {

    VersionDAO vdao = new VersionDAO(conn);

    try {
      logger.debug("Starting import: " + importVersionId + " : " + archive);

      FileCacheManager fcm = new FileCacheManager(archive);

      boolean validated = false;
      ArrayList<CSVParserException> errors = null;
      ArrayList<CSVParserException> warnings = null;

      try {
        vdao.startUpload(importVersionId, userId);
        conn.commit(); // saves the started status and empty clob

        logger.debug("Start Extract");
        fcm.extract(tmpDir);
        logger.debug("Extract complete");

        // only validate if we can read the files
        logger.debug("Start archive validation");

        ArchiveFormatValidator afv = new ArchiveFormatValidator(fcm);

        validated = afv.validate();
        logger.debug("Validation Completed: " + validated);

        errors = afv.getErrors();
        warnings = afv.getWarnings();

      } catch (IOException e) {
        errors = new ArrayList<>(); // always null
        errors.add(new CSVParserException(0, 0, e));
      }

      if (errors == null) {
        errors = new ArrayList<>();
      }

      if (errors.size() == 0) {

        // skip loading if there were errors with the package
        load(fcm, errors, userId, importVersionId);
      }

      logger.debug("updating import version");
      vdao.updateControlFileInfo(importVersionId, userId);
      vdao.uploadedVersion(importVersionId, ImportLogFormatter
          .formatStagingXml(errors, warnings, conn), new Boolean(!(errors
          .size() == 0)
          || !validated), userId);
      logger.debug("Completed csv load");

    } catch (SQLException e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);

      String xml = ImportLogFormatter.formatUploadException(e);
      vdao.uploadFailed(importVersionId, xml, userId);
    } catch (IOException e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);

      String xml = ImportLogFormatter.formatUploadException(e);
      vdao.uploadFailed(importVersionId, xml, userId);
    }
  }

  /**
   * 
   * @param pFcm FileCacheManager
   * @param pErrors ArrayList
   * @param userId String
   * @param importVersionId Integer
   */
  private void load(final FileCacheManager pFcm, final ArrayList<CSVParserException> pErrors,
      final String userId, final Integer importVersionId) {
    StagingDAO sdao = new StagingDAO(conn);

    logger.debug("clearing staging");
    status(sdao, importVersionId, "Clearing Staging", pErrors);

    Date start = new Date();

    try {
      sdao.clear();
      conn.commit();
    } catch (SQLException e) {
      pErrors.add(new CSVParserException(0, 0, e));

      return; // can't carry on
    }

    try {
      logger.debug("Loading 99");
      status(sdao, importVersionId, "Loading 99", pErrors);
      load(pFcm, FileCacheManager.FIPD_99_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 01");
      status(sdao, importVersionId, "Loading 01", pErrors);
      load(pFcm, FileCacheManager.FIPD_01_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 02");
      status(sdao, importVersionId, "Loading 02", pErrors);
      load(pFcm, FileCacheManager.FIPD_02_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 03");
      status(sdao, importVersionId, "Loading 03", pErrors);
      load(pFcm, FileCacheManager.FIPD_03_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 04");
      status(sdao, importVersionId, "Loading 04", pErrors);
      load(pFcm, FileCacheManager.FIPD_04_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 05");
      status(sdao, importVersionId, "Loading 05", pErrors);
      load(pFcm, FileCacheManager.FIPD_05_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 42");
      status(sdao, importVersionId, "Loading 42", pErrors);
      load(pFcm, FileCacheManager.FIPD_42_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 22");
      status(sdao, importVersionId, "Loading 22", pErrors);
      load(pFcm, FileCacheManager.FIPD_22_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 23");
      status(sdao, importVersionId, "Loading 23", pErrors);
      load(pFcm, FileCacheManager.FIPD_23_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 28");
      status(sdao, importVersionId, "Loading 28", pErrors);
      load(pFcm, FileCacheManager.FIPD_28_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 29");
      status(sdao, importVersionId, "Loading 29", pErrors);
      load(pFcm, FileCacheManager.FIPD_29_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 21");
      status(sdao, importVersionId, "Loading 21", pErrors);
      load(pFcm, FileCacheManager.FIPD_21_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 40");
      status(sdao, importVersionId, "Loading 40", pErrors);
      load(pFcm, FileCacheManager.FIPD_40_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 50");
      status(sdao, importVersionId, "Loading 50", pErrors);
      load(pFcm, FileCacheManager.FIPD_50_NAME_PREFIX, sdao, pErrors, userId);

      logger.debug("Loading 51");
      status(sdao, importVersionId, "Loading 51", pErrors);
      load(pFcm, FileCacheManager.FIPD_51_NAME_PREFIX, sdao, pErrors, userId);
      
      double minutes = ((int) (((new Date()).getTime() - start.getTime()) / (1000.0 * 60)) * 100) / 100.0;
      status(sdao, importVersionId, "Staging completed in " + minutes
          + " minutes.", pErrors);
    } finally {
      try {
        sdao.close();
      } catch (SQLException e) {
        pErrors.add(new CSVParserException(0, 0, e));
      }
    }
  }

  /**
   * @param pSdao StagingDAO
   * @param pImportVersionId Integer
   * @param pString String
   * @param pErrors ArrayList
   */
  private void status(StagingDAO pSdao, Integer pImportVersionId,
      String pString, final ArrayList<CSVParserException> pErrors) {
    try {
      pSdao.status(pImportVersionId, pString);
    } catch (SQLException e) {
      pErrors.add(new CSVParserException(0, 0, e));
    }
  }

  /**
   * @param pFcm FileCacheManager
   * @param name File Name Prefix
   * @param sdao StagingDAO
   * @param pErrors return by reference; List of CSVParserException
   * @param userId userId
   */
  private void load(final FileCacheManager pFcm, final String name,
      final StagingDAO sdao, final ArrayList<CSVParserException> pErrors, final String userId) {
    FileHandle fileHandle = null;

    if (pErrors.size() >= errorsAllowed) {
      return;
    }

    try {
      fileHandle = pFcm.read(name);

      while (fileHandle.hasNext()) {

        try {

          switch (fileHandle.getFileNumber().intValue()) {

          case FileCacheManager.FIPD_01_NUMBER:
            sdao.insert((Z01ParticipantInfo) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_02_NUMBER:
            sdao.insert((Z02PartpntFarmInfo) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_03_NUMBER:
            sdao.insert((Z03StatementInfo) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_04_NUMBER:
            sdao.insert((Z04IncomeExpsDtl) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_05_NUMBER:
            sdao.insert((Z05PartnerInfo) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_21_NUMBER:
            sdao.insert((Z21ParticipantSuppl) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_22_NUMBER:
            sdao.insert((Z22ProductionInsurance) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_23_NUMBER:
            sdao.insert((Z23LivestockProdCpct) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_28_NUMBER:
            sdao.insert((Z28ProdInsuranceRef) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_29_NUMBER:
            sdao.insert((Z29InventoryRef) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_40_NUMBER:
            sdao.insert((Z40PrtcpntRefSuplDtl) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_42_NUMBER:
            sdao.insert((Z42ParticipantRefYear) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_50_NUMBER:
            sdao.insert((Z50ParticipntBnftCalc) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_51_NUMBER:
            sdao.insert((Z51ParticipantContrib) fileHandle.next(), userId);

            break;

          case FileCacheManager.FIPD_99_NUMBER:
            sdao.insert((Z99ExtractFileCtl) fileHandle.next(), userId);

            break;

          default:
            pErrors.add(new CSVParserException(0, fileHandle.getFileNumber()
                .intValue(), "Un-expected File Handle Found"));

            if (pErrors.size() >= errorsAllowed) {
              return;
            }
          }
        } catch (SQLException e) {
          pErrors.add(new CSVParserException(fileHandle.getRowsRead(),
              fileHandle.getFileNumber().intValue(), e));

          if (pErrors.size() >= errorsAllowed) {
            return;
          }
        } catch (RuntimeException e) {
          pErrors.add(new CSVParserException(fileHandle.getRowsRead(),
              fileHandle.getFileNumber().intValue(), e));

          if (pErrors.size() >= errorsAllowed) {
            return;
          }
        }
      }
    } catch (CSVParserException e) {
      pErrors.add(e);
    } finally {

      if (fileHandle != null) {

        try {
          fileHandle.close();
        } catch (IOException e) {
          pErrors.add(new CSVParserException(0, fileHandle.getFileNumber()
              .intValue(), e));
        }
      }

      try {
        conn.commit();
      } catch (SQLException e) {
        pErrors.add(new CSVParserException(0, (fileHandle == null) ? 0
            : fileHandle.getFileNumber().intValue(), e));
      }
    }
  }

  /**
   * @param pImportVersionId pImportVersionId
   * @param userId userId
   * 
   * @see ImportController#processImport(java.lang.Integer, String)
   */
  @Override
  public void processImport(final Integer pImportVersionId, final String userId) {
    VersionDAO vdao = new VersionDAO(conn);

    try {
      vdao.startImport(pImportVersionId, userId);
      conn.commit(); // saves the started status and empty clob

      vdao.performImport(pImportVersionId, userId);
      conn.commit(); // saves the work
      
      CacheUtils.refreshYearConfigLists();
      
    } catch (SQLException | ServiceException e) {
      logger.error("Unexpected error: ", e);

      try {
        conn.rollback();

        String xml = ImportLogFormatter.formatImportException(e);
        vdao.importFailed(pImportVersionId, xml, userId);
        conn.commit(); // saves the failure state
      } catch (SQLException | IOException e1) {
        logger.error("Error updating log XML for CRA import", e1);
      }
    }
    
    try {
      CacheUtils.refreshYearConfigLists();
    } catch (ServiceException e) {
      logger.error("Error refreshing year config lists after import: ", e);
    }
  }

}
