package ca.bc.gov.farms.service.api.v1.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.farms.csv.BpuFileHandle;
import ca.bc.gov.farms.csv.CSVParserException;
import ca.bc.gov.farms.persistence.v1.dao.ImportBPUDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportVersionDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportBPUDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportVersionDaoImpl;
import ca.bc.gov.farms.persistence.v1.dto.ImportBPUDto;
import ca.bc.gov.farms.service.api.v1.ImportBPUService;

public class ImportBPUServiceImpl extends BaseServiceImpl implements ImportBPUService {

    private static final Logger logger = LoggerFactory.getLogger(ImportBPUServiceImpl.class);

    private List<Object> stagingErrors = new ArrayList<>();

    public ImportBPUServiceImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Object> getStagingErrors() {
        return stagingErrors;
    }

    @Override
    public void importCSV(Long importVersionId, InputStream inputStream, String userId) {
        Connection connection = null;
        ImportVersionDao vdao = null;
        ImportBPUDao bpuDao = null;

        try {
            connection = getConnection();
            vdao = new ImportVersionDaoImpl(connection);
            bpuDao = new ImportBPUDaoImpl(connection);

            // begin
            stagingErrors.clear();
            connection.setAutoCommit(false);

            bpuDao.clearStaging();
            connection.commit();

            vdao.startUpload(importVersionId, userId);
            connection.commit();

            // validate headers
            BpuFileHandle fileHandle = new BpuFileHandle(inputStream);
            String[] fileErrors = fileHandle.validate();
            int numRows = 0;

            if (fileErrors == null || fileErrors.length == 0) {

                // upload the file
                numRows = uploadToStaging(userId, bpuDao, fileHandle);

                // validate what is in the staging table
                bpuDao.validateStaging(importVersionId);

                List<String> valErrors = bpuDao.getStagingErrors(importVersionId);
                bpuDao.deleteStagingErrors(importVersionId);

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
            vdao.uploadedVersion(importVersionId, xml, hasErrors, userId);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected error: ", e);

            try {
                if (connection != null) {
                    connection.rollback();
                    if (vdao != null) {
                        String xml = ImportLogFormatter.formatImportException(e);
                        vdao.importFailure(importVersionId, xml, userId);
                        connection.commit();
                    }
                }
            } catch (Exception e2) {
                logger.error("Unexpected error: ", e2);
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }

    @Override
    public void processImport(Long importVersionId, String userId) {
        Connection connection = null;
        ImportVersionDao vdao = null;
        ImportBPUDao bpuDao = null;

        try {
            connection = getConnection();
            vdao = new ImportVersionDaoImpl(connection);
            bpuDao = new ImportBPUDaoImpl(connection);

            // begin
            connection.setAutoCommit(false);
            vdao.startImport(importVersionId, userId);
            connection.commit();

            bpuDao.performImport(importVersionId, userId);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected error: ", e);

            try {
                if (connection != null) {
                    connection.rollback();
                    if (vdao != null) {
                        String xml = ImportLogFormatter.formatImportException(e);
                        vdao.importFailure(importVersionId, xml, userId);
                        connection.commit();
                    }
                }
            } catch (Exception e1) {
                logger.error("Error updating log XML for BPU import", e1);
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection", e);
                }
            }
        }
    }

    private int uploadToStaging(String userId, ImportBPUDao bpuDao, BpuFileHandle fileHandle) {
        List<ImportBPUDto> records = fileHandle.getRecords();

        // first row is a header
        final int startRowNum = 2;
        int rowNum = startRowNum;

        for (ImportBPUDto dto : records) {
            try {
                bpuDao.insertStagingRow(dto, userId, rowNum);
            } catch (Exception ex) {
                ex.printStackTrace();
                stagingErrors.add(new CSVParserException(rowNum, 0, ex));
            }
            rowNum++;
        }

        return rowNum - startRowNum;
    }

}
