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
import ca.bc.gov.farms.csv.IvprFileHandle;
import ca.bc.gov.farms.persistence.v1.dao.ImportIVPRDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportVersionDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportIVPRDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportVersionDaoImpl;
import ca.bc.gov.farms.persistence.v1.dto.ImportIVPRDto;
import ca.bc.gov.farms.service.api.v1.ImportIVPRService;

public class ImportIVPRServiceImpl extends BaseServiceImpl implements ImportIVPRService {

    private static final Logger logger = LoggerFactory.getLogger(ImportIVPRServiceImpl.class);

    private List<Object> stagingErrors = new ArrayList<>();

    public ImportIVPRServiceImpl(DataSource dataSource) {
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
        ImportIVPRDao ivprDao = null;

        try {
            connection = getConnection();
            vdao = new ImportVersionDaoImpl(connection);
            ivprDao = new ImportIVPRDaoImpl(connection);

            // begin
            stagingErrors.clear();
            connection.setAutoCommit(false);

            ivprDao.clearStaging();
            connection.commit();

            vdao.startUpload(importVersionId, userId);
            connection.commit();

            // validate headers
            IvprFileHandle fileHandle = new IvprFileHandle(inputStream);
            String[] fileErrors = fileHandle.validate();
            int numRows = 0;

            if (fileErrors == null || fileErrors.length == 0) {

                // upload the file
                numRows = uploadToStaging(userId, ivprDao, fileHandle);

                // validate what is in the staging table
                ivprDao.validateStaging(importVersionId);

                List<String> valErrors = ivprDao.getStagingErrors(importVersionId);
                ivprDao.deleteStagingErrors(importVersionId);

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
        ImportIVPRDao ivprDao = null;

        try {
            connection = getConnection();
            vdao = new ImportVersionDaoImpl(connection);
            ivprDao = new ImportIVPRDaoImpl(connection);

            // begin
            connection.setAutoCommit(false);
            vdao.startImport(importVersionId, userId);
            connection.commit();

            ivprDao.performImport(importVersionId, userId);
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
                logger.error("Error updating log XML for IVPR import", e1);
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

    private int uploadToStaging(String userId, ImportIVPRDao ivprDao, IvprFileHandle fileHandle) {
        List<ImportIVPRDto> records = fileHandle.getRecords();

        // first row is a header
        final int startRowNum = 2;
        int rowNum = startRowNum;

        for (ImportIVPRDto dto : records) {
            try {
                ivprDao.insertStagingRow(dto, userId, rowNum);
            } catch (Exception ex) {
                ex.printStackTrace();
                stagingErrors.add(new CSVParserException(rowNum, 0, ex));
            }
            rowNum++;
        }

        return rowNum - startRowNum;
    }

}
