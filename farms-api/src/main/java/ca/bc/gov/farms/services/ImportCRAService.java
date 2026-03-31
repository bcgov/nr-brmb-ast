package ca.bc.gov.farms.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.farms.data.entities.staging.Z01ParticipantInfo;
import ca.bc.gov.farms.data.entities.staging.Z02PartpntFarmInfo;
import ca.bc.gov.farms.data.entities.staging.Z03StatementInfo;
import ca.bc.gov.farms.data.entities.staging.Z04IncomeExpsDtl;
import ca.bc.gov.farms.data.entities.staging.Z05PartnerInfo;
import ca.bc.gov.farms.data.entities.staging.Z21ParticipantSuppl;
import ca.bc.gov.farms.data.entities.staging.Z22ProductionInsurance;
import ca.bc.gov.farms.data.entities.staging.Z23LivestockProdCpct;
import ca.bc.gov.farms.data.entities.staging.Z28ProdInsuranceRef;
import ca.bc.gov.farms.data.entities.staging.Z29InventoryRef;
import ca.bc.gov.farms.data.entities.staging.Z40PrtcpntRefSuplDtl;
import ca.bc.gov.farms.data.entities.staging.Z42ParticipantRefYear;
import ca.bc.gov.farms.data.entities.staging.Z50ParticipntBnftCalc;
import ca.bc.gov.farms.data.entities.staging.Z51ParticipantContrib;
import ca.bc.gov.farms.data.entities.staging.Z99ExtractFileCtl;
import ca.bc.gov.farms.data.repositories.ErrorRepository;
import ca.bc.gov.farms.data.repositories.ImportVersionRepository;
import ca.bc.gov.farms.data.repositories.StagingRepository;
import ca.bc.gov.farms.services.csv.ArchiveFormatValidator;
import ca.bc.gov.farms.services.csv.CSVParserException;
import ca.bc.gov.farms.services.csv.FileCacheManager;
import ca.bc.gov.farms.services.csv.FileHandle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImportCRAService {

    @Autowired
    private ImportVersionRepository importVersionRepository;

    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private StagingRepository stagingRepository;

    private static final int DEFAULT_ERRORS = 250000;

    @Transactional
    public void importCSV(Long importVersionId, InputStream archive, String userId) {

        try {
            log.debug("Starting import: " + importVersionId);

            FileCacheManager fcm = new FileCacheManager(archive);

            boolean validated = false;
            List<CSVParserException> errors = null;
            List<CSVParserException> warnings = null;

            try {
                importVersionRepository.startUpload(importVersionId, userId);

                log.debug("Start Extract");
                fcm.extract();
                log.debug("Extract complete");

                // only validate if we can read the files
                log.debug("Start archive validation");

                ArchiveFormatValidator afv = new ArchiveFormatValidator(fcm);

                validated = afv.validate();
                log.debug("Validation Completed: " + validated);

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

            log.debug("updating import version");
            importVersionRepository.updateControlFileInfoStg(importVersionId, userId);
            importVersionRepository.uploadedVersion(
                    importVersionId,
                    ImportLogFormatter.formatStagingXml(errors, warnings, errorRepository),
                    Boolean.valueOf(!(errors.size() == 0) || !validated),
                    userId);
            log.debug("Completed csv load");
        } catch (Exception e) {
            log.error("Unexpected error: ", e);

            String xml = ImportLogFormatter.formatUploadException(e);
            importVersionRepository.uploadFailure(importVersionId, xml, userId);

            // trigger rollback
            throw new RuntimeException(e);
        }
    }

    private void load(final FileCacheManager pFcm, final List<CSVParserException> pErrors, final String userId,
            final Long importVersionId) {

        log.debug("clearing staging");
        status(stagingRepository, importVersionId, "Clearing Staging", pErrors);

        Date start = new Date();

        try {
            stagingRepository.clear();
        } catch (Exception e) {
            pErrors.add(new CSVParserException(0, 0, e));
            return; // can't carry on
        }

        log.debug("Loading 99");
        status(stagingRepository, importVersionId, "Loading 99", pErrors);
        load(pFcm, FileCacheManager.FIPD_99_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 01");
        status(stagingRepository, importVersionId, "Loading 01", pErrors);
        load(pFcm, FileCacheManager.FIPD_01_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 02");
        status(stagingRepository, importVersionId, "Loading 02", pErrors);
        load(pFcm, FileCacheManager.FIPD_02_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 03");
        status(stagingRepository, importVersionId, "Loading 03", pErrors);
        load(pFcm, FileCacheManager.FIPD_03_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 04");
        status(stagingRepository, importVersionId, "Loading 04", pErrors);
        load(pFcm, FileCacheManager.FIPD_04_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 05");
        status(stagingRepository, importVersionId, "Loading 05", pErrors);
        load(pFcm, FileCacheManager.FIPD_05_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 42");
        status(stagingRepository, importVersionId, "Loading 42", pErrors);
        load(pFcm, FileCacheManager.FIPD_42_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 22");
        status(stagingRepository, importVersionId, "Loading 22", pErrors);
        load(pFcm, FileCacheManager.FIPD_22_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 23");
        status(stagingRepository, importVersionId, "Loading 23", pErrors);
        load(pFcm, FileCacheManager.FIPD_23_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 28");
        status(stagingRepository, importVersionId, "Loading 28", pErrors);
        load(pFcm, FileCacheManager.FIPD_28_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 29");
        status(stagingRepository, importVersionId, "Loading 29", pErrors);
        load(pFcm, FileCacheManager.FIPD_29_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 21");
        status(stagingRepository, importVersionId, "Loading 21", pErrors);
        load(pFcm, FileCacheManager.FIPD_21_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 40");
        status(stagingRepository, importVersionId, "Loading 40", pErrors);
        load(pFcm, FileCacheManager.FIPD_40_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 50");
        status(stagingRepository, importVersionId, "Loading 50", pErrors);
        load(pFcm, FileCacheManager.FIPD_50_NAME_PREFIX, stagingRepository, pErrors, userId);

        log.debug("Loading 51");
        status(stagingRepository, importVersionId, "Loading 51", pErrors);
        load(pFcm, FileCacheManager.FIPD_51_NAME_PREFIX, stagingRepository, pErrors, userId);

        double minutes = ((int) (((new Date()).getTime() - start.getTime()) / (1000.0 * 60)) * 100) / 100.0;
        status(stagingRepository, importVersionId, "Staging completed in " + minutes + " minutes.", pErrors);
    }

    private void status(StagingRepository pSdao, Long pImportVersionId, String pString,
            final List<CSVParserException> pErrors) {
        try {
            pSdao.status(pImportVersionId, pString);
        } catch (Exception e) {
            pErrors.add(new CSVParserException(0, 0, e));
        }
    }

    private void load(final FileCacheManager pFcm, final String name, final StagingRepository sdao,
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
            log.debug("Number of records: " + records.size());
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
                } catch (RuntimeException e) {
                    pErrors.add(new CSVParserException(fileHandle.getRowsRead(), fileHandle.getFileNumber(), e));
                    if (pErrors.size() >= DEFAULT_ERRORS) {
                        return;
                    }
                }
            }
        } catch (CSVParserException e) {
            pErrors.add(e);
        }
    }

    @Transactional
    public void processImport(Long pImportVersionId, String userId) {

        try {
            importVersionRepository.startImport(pImportVersionId, userId);

            importVersionRepository.performImport(pImportVersionId, userId);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);

            String xml = ImportLogFormatter.formatImportException(e);
            importVersionRepository.importFailure(pImportVersionId, xml, userId);

            // trigger rollback
            throw new RuntimeException(e);
        }
    }
}
