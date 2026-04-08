package ca.bc.gov.farms.services;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.farms.data.entities.ImportFMVEntity;
import ca.bc.gov.farms.data.repositories.ImportFMVRepository;
import ca.bc.gov.farms.data.repositories.ImportVersionRepository;
import ca.bc.gov.farms.services.csv.CSVParserException;
import ca.bc.gov.farms.services.csv.FmvFileHandle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImportFMVService {

    @Autowired
    private ImportVersionRepository importVersionRepository;

    @Autowired
    private ImportFMVRepository importFMVRepository;

    private List<Object> stagingErrors = new ArrayList<>();

    public List<Object> getStagingErrors() {
        return stagingErrors;
    }

    @Transactional
    public void importCSV(Long importVersionId, InputStream inputStream, String userId) {

        stagingErrors.clear();

        try {
            // begin
            importFMVRepository.clearStaging();

            importVersionRepository.startUpload(importVersionId, userId);

            // validate headers
            FmvFileHandle fileHandle = new FmvFileHandle(inputStream);
            String[] fileErrors = fileHandle.validate();
            int numRows = 0;

            if (fileErrors == null || fileErrors.length == 0) {

                // upload the file
                numRows = uploadToStaging(userId, importFMVRepository, fileHandle);

                // validate what is in the staging table
                importFMVRepository.validateStaging(importVersionId);

                List<String> valErrors = importFMVRepository.getStagingErrors(importVersionId);
                importFMVRepository.deleteStagingErrors(importVersionId);

                stagingErrors.addAll(valErrors);
            } else {

                // convert the validation errors
                for (int ii = 0; ii < fileErrors.length; ii++) {
                    String msg = fileErrors[ii];
                    CSVParserException pe = new CSVParserException(1, 0, msg);
                    stagingErrors.add(pe);
                }
            }

            // end
            String xml = ImportLogFormatter.createStagingXml(numRows, stagingErrors);
            Boolean hasErrors = Boolean.valueOf(stagingErrors.size() != 0);
            importVersionRepository.uploadedVersion(importVersionId, xml, hasErrors, userId);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);

            String xml = ImportLogFormatter.formatImportException(e);
            importVersionRepository.importFailure(importVersionId, xml, userId);

            // trigger rollback
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void processImport(Long importVersionId, String userId) {

        try {
            importVersionRepository.startImport(importVersionId, userId);

            importFMVRepository.performImport(importVersionId, userId);
        } catch (Exception e) {
            log.error("Unexpected error: ", e);

            String xml = ImportLogFormatter.formatImportException(e);
            importVersionRepository.importFailure(importVersionId, xml, userId);

            // trigger rollback
            throw new RuntimeException(e);
        }
    }

    private int uploadToStaging(String userId, ImportFMVRepository fmvDao, FmvFileHandle fileHandle) {
        List<ImportFMVEntity> records = fileHandle.getRecords();

        // first row is a header
        final int startRowNum = 2;
        int rowNum = startRowNum;

        for (ImportFMVEntity dto : records) {
            try {
                fmvDao.insertStagingRow(dto, userId, rowNum);
            } catch (Exception ex) {
                ex.printStackTrace();
                stagingErrors.add(new CSVParserException(rowNum, 0, ex));
            }
            rowNum++;
        }

        return rowNum - startRowNum;
    }
}
