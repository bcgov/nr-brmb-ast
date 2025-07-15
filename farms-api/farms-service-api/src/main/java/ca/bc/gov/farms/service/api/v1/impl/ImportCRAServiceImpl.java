package ca.bc.gov.farms.service.api.v1.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.csv.ArchiveFormatValidator;
import ca.bc.gov.farms.csv.CSVParserException;
import ca.bc.gov.farms.csv.FileCacheManager;
import ca.bc.gov.farms.csv.FileHandle;
import ca.bc.gov.farms.persistence.v1.dao.ImportVersionDao;
import ca.bc.gov.farms.persistence.v1.dao.StagingDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportVersionDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.StagingDaoImpl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z01ParticipantInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z02PartpntFarmInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z03StatementInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z04IncomeExpsDtl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z05PartnerInfo;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z21ParticipantSuppl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z22ProductionInsurance;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z23LivestockProdCpct;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z28ProdInsuranceRef;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z29InventoryRef;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z40PrtcpntRefSuplDtl;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z42ParticipantRefYear;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z50ParticipntBnftCalc;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z51ParticipantContrib;
import ca.bc.gov.farms.persistence.v1.dto.staging.Z99ExtractFileCtl;
import ca.bc.gov.farms.service.api.v1.ImportCRAService;

public class ImportCRAServiceImpl extends BaseServiceImpl implements ImportCRAService {

    private static final Logger logger = LoggerFactory.getLogger(ImportCRAServiceImpl.class);

    private static final int DEFAULT_ERRORS = 250000;

    private Connection conn;

    public ImportCRAServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void importCSV(Long importVersionId, InputStream archive, String userId) {
        ImportVersionDao vdao = null;

        try {
            conn = getConnection();
            vdao = new ImportVersionDaoImpl(conn);

            logger.debug("Starting import: " + importVersionId);

            FileCacheManager fcm = new FileCacheManager(archive);

            boolean validated = false;
            List<CSVParserException> errors = null;
            List<CSVParserException> warnings = null;

            try {
                vdao.startUpload(importVersionId, userId);
                conn.commit(); // saves the started status and empty clob

                logger.debug("Start Extract");
                fcm.extract();
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
            vdao.updateControlFileInfoStg(importVersionId, userId);
            vdao.uploadedVersion(
                    importVersionId,
                    ImportLogFormatter.formatStagingXml(errors, warnings, conn),
                    new Boolean(!(errors.size() == 0) || !validated),
                    userId);
            logger.debug("Completed csv load");
        } catch (SQLException | DaoException e) {
            e.printStackTrace();
            logger.error("Unexpected error: ", e);

            String xml = ImportLogFormatter.formatUploadException(e);
            vdao.uploadFailure(importVersionId, xml, userId);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }

    private void load(final FileCacheManager pFcm, final List<CSVParserException> pErrors, final String userId,
            final Long importVersionId) {
        StagingDao sdao = new StagingDaoImpl(conn);

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
        status(sdao, importVersionId, "Staging completed in " + minutes + " minutes.", pErrors);
    }

    private void status(StagingDao pSdao, Long pImportVersionId, String pString,
            final List<CSVParserException> pErrors) {
        try {
            pSdao.status(pImportVersionId, pString);
        } catch (SQLException e) {
            pErrors.add(new CSVParserException(0, 0, e));
        }
    }

    private void load(final FileCacheManager pFcm, final String name, final StagingDao sdao,
            final List<CSVParserException> pErrors, final String userId) {
        FileHandle<?> fileHandle = null;

        if (pErrors.size() > DEFAULT_ERRORS) {
            return;
        }

        try {
            fileHandle = pFcm.read(name);
            if (fileHandle == null) {
                pErrors.add(new CSVParserException(0, 0, "Un-expected File Handle Found"));
                return;
            }

            List<?> records = fileHandle.getRecords();
            logger.debug("Number of records: " + records.size());
            for (Object record : records) {
                try {
                    if (record instanceof Z01ParticipantInfo) {
                        sdao.insert((Z01ParticipantInfo) record, userId);
                    } else if (record instanceof Z02PartpntFarmInfo) {
                        sdao.insert((Z02PartpntFarmInfo) record, userId);
                    } else if (record instanceof Z03StatementInfo) {
                        sdao.insert((Z03StatementInfo) record, userId);
                    } else if (record instanceof Z04IncomeExpsDtl) {
                        sdao.insert((Z04IncomeExpsDtl) record, userId);
                    } else if (record instanceof Z05PartnerInfo) {
                        sdao.insert((Z05PartnerInfo) record, userId);
                    } else if (record instanceof Z21ParticipantSuppl) {
                        sdao.insert((Z21ParticipantSuppl) record, userId);
                    } else if (record instanceof Z22ProductionInsurance) {
                        sdao.insert((Z22ProductionInsurance) record, userId);
                    } else if (record instanceof Z23LivestockProdCpct) {
                        sdao.insert((Z23LivestockProdCpct) record, userId);
                    } else if (record instanceof Z28ProdInsuranceRef) {
                        sdao.insert((Z28ProdInsuranceRef) record, userId);
                    } else if (record instanceof Z29InventoryRef) {
                        sdao.insert((Z29InventoryRef) record, userId);
                    } else if (record instanceof Z40PrtcpntRefSuplDtl) {
                        sdao.insert((Z40PrtcpntRefSuplDtl) record, userId);
                    } else if (record instanceof Z42ParticipantRefYear) {
                        sdao.insert((Z42ParticipantRefYear) record, userId);
                    } else if (record instanceof Z50ParticipntBnftCalc) {
                        sdao.insert((Z50ParticipntBnftCalc) record, userId);
                    } else if (record instanceof Z51ParticipantContrib) {
                        sdao.insert((Z51ParticipantContrib) record, userId);
                    } else if (record instanceof Z99ExtractFileCtl) {
                        sdao.insert((Z99ExtractFileCtl) record, userId);
                    }
                } catch (SQLException | RuntimeException e) {
                    pErrors.add(new CSVParserException(fileHandle.getRowsRead(), fileHandle.getFileNumber(), e));
                    if (pErrors.size() >= DEFAULT_ERRORS) {
                        return;
                    }
                }
            }
        } catch (CSVParserException e) {
            pErrors.add(e);
        } finally {
            try {
                conn.commit();
            } catch (SQLException e) {
                pErrors.add(new CSVParserException(0, fileHandle == null ? 0 : fileHandle.getFileNumber(), e));
            }
        }
    }

    @Override
    public void processImport(Long pImportVersionId, String userId) {
        ImportVersionDao vdao = null;

        try {
            conn = getConnection();
            vdao = new ImportVersionDaoImpl(conn);

            vdao.startImport(pImportVersionId, userId);
            conn.commit(); // saves the started status and empty clob

            vdao.performImport(pImportVersionId, userId);
            conn.commit(); // saves the work

            vdao.analyzeSchema(pImportVersionId);
            // no need to do a commit here
        } catch (SQLException | DaoException e) {
            logger.error("Unexpected error: ", e);

            try {
                conn.rollback();

                String xml = ImportLogFormatter.formatImportException(e);
                vdao.importFailure(pImportVersionId, xml, userId);
                conn.commit(); // saves the failure state
            } catch (SQLException | DaoException e1) {
                logger.error("Error updating log XML for CRA import", e1);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }
}
