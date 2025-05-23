package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportBPUDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.ImportBPUMapper;
import ca.bc.gov.farms.persistence.v1.dto.ImportBPUDto;
import ca.bc.gov.farms.persistence.v1.dto.ImportLogDto;

public class ImportBPUDaoImpl extends BaseDao implements ImportBPUDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ImportBPUDaoImpl.class);

    @Autowired
    private ImportBPUMapper mapper;

    @Override
    public void insertStagingRow(ImportBPUDto dto, String userId) throws DaoException {
        logger.debug("<insertStagingRow");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("dto", dto);
            parameters.put("userId", userId);
            this.mapper.insertStagingRow(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insertStagingRow");
    }

    @Override
    public void clearStaging() throws DaoException {
        logger.debug("<clearStaging");

        try {
            this.mapper.clearStaging();
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">clearStaging");
    }

    @Override
    public void deleteStagingErrors(Long importVersionId) throws DaoException {
        logger.debug("<deleteStagingErrors");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("importVersionId", importVersionId);
            this.mapper.deleteStagingErrors(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">deleteStagingErrors");
    }

    @Override
    public void validateStaging(Long importVersionId) throws DaoException {
        logger.debug("<validateStaging");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("importVersionId", importVersionId);
            this.mapper.validateStaging(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">validateStaging");
    }

    @Override
    public List<ImportLogDto> getStagingErrors(Long importVersionId) throws DaoException {
        logger.debug("<getStagingErrors");

        List<ImportLogDto> result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("importVersionId", importVersionId);
            result = this.mapper.getStagingErrors(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">getStagingErrors");
        return result;
    }

    @Override
    public void stagingToOperational(Long importVersionId, String userId) throws DaoException {
        logger.debug("<stagingToOperational");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("importVersionId", importVersionId);
            parameters.put("userId", userId);
            this.mapper.stagingToOperational(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">stagingToOperational");
    }

}
