package ca.bc.gov.farms.service.api.v1.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.farms.csv.CSVParserException;
import ca.bc.gov.farms.csv.FmvFileHandle;
import ca.bc.gov.farms.persistence.v1.dao.ImportFMVDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportVersionDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportFMVDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportVersionDaoImpl;
import ca.bc.gov.farms.persistence.v1.dto.ImportFMVDto;
import ca.bc.gov.farms.service.api.v1.ImportFMVService;

public class ImportFMVServiceImpl extends BaseServiceImpl implements ImportFMVService {

    private static final Logger logger = LoggerFactory.getLogger(ImportFMVServiceImpl.class);

    private List<Object> stagingErrors = new ArrayList<>();

    public ImportFMVServiceImpl(DataSource dataSource) {
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
        ImportFMVDao fmvDao = null;

        try {
            connection = getConnection();
            vdao = new ImportVersionDaoImpl(connection);
            fmvDao = new ImportFMVDaoImpl(connection);

            // begin
            stagingErrors.clear();
            connection.setAutoCommit(false);

            fmvDao.clearStaging();
            connection.commit();

            vdao.startUpload(importVersionId, userId);
            connection.commit();

            // validate headers
            FmvFileHandle fileHandle = new FmvFileHandle(inputStream);
            String[] fileErrors = fileHandle.validate();
            int numRows = 0;

            if (fileErrors == null || fileErrors.length == 0) {

                // upload the file
                numRows = uploadToStaging(userId, fmvDao, fileHandle);

                // validate what is in the staging table
                fmvDao.validateStaging(importVersionId);

                List<String> valErrors = fmvDao.getStagingErrors(importVersionId);
                fmvDao.deleteStagingErrors(importVersionId);

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
            Boolean hasErrors = new Boolean(stagingErrors.size() != 0);
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
        ImportFMVDao fmvDao = null;

        try {
            connection = getConnection();
            vdao = new ImportVersionDaoImpl(connection);
            fmvDao = new ImportFMVDaoImpl(connection);

            // begin
            connection.setAutoCommit(false);
            vdao.startImport(importVersionId, userId);
            connection.commit();

            fmvDao.performImport(importVersionId, userId);
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
                logger.error("Error updating log XML for FMV import", e1);
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

    private int uploadToStaging(String userId, ImportFMVDao fmvDao, FmvFileHandle fileHandle) {
        List<ImportFMVDto> records = fileHandle.getRecords();

        // first row is a header
        final int startRowNum = 2;
        int rowNum = startRowNum;

        for (ImportFMVDto dto : records) {
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
