package ca.bc.gov.farms.service.api.v1.impl;

import java.sql.Connection;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.persistence.v1.dao.ImportDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ImportDaoImpl;
import ca.bc.gov.farms.persistence.v1.dto.ImportVersionDto;
import ca.bc.gov.farms.service.api.v1.ImportService;

public class ImportServiceImpl implements ImportService {

    private static final Logger logger = LoggerFactory.getLogger(ImportServiceImpl.class);

    private DataSource dataSource;

    public ImportServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ImportVersionDto createImportVersion(String importClassCode, String importStateCode, String description,
            String fileName, byte[] fileContent, String userId) throws ServiceException {
        Connection connection = null;
        ImportDao importDao = null;

        try {
            connection = dataSource.getConnection();
            importDao = new ImportDaoImpl(connection);

            ImportVersionDto importVersionDto = new ImportVersionDto();
            importVersionDto.setImportClassCode(importClassCode);
            importVersionDto.setImportStateCode(importStateCode);
            importVersionDto.setDescription(description);
            importVersionDto.setImportFileName(fileName);
            importVersionDto.setImportFile(fileContent);
            importVersionDto.setImportedByUser(userId);

            importDao.insertImportVersion(importVersionDto);
            return importVersionDto;
        } catch (Exception e) {
            logger.error("Error creating import version", e);
            throw new ServiceException("Failed to create import version", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.warn("Failed to close connection", e);
                }
            }
        }
    }

}
