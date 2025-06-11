package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportVersionDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.ImportVersionMapper;

public class ImportVersionDaoImpl extends BaseDao implements ImportVersionDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ImportVersionDaoImpl.class);

    @Autowired
    private ImportVersionMapper mapper;

    @Override
    public Integer createVersion(String description, String importFileName, String user) throws DaoException {
        logger.debug("<createVersion");

        Integer versionId = null;
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("description", description);
            parameters.put("importFileName", importFileName);
            parameters.put("user", user);
            versionId = this.mapper.createVersion(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">createVersion: " + versionId);
        return versionId;
    }

    @Override
    public void updateControlFileInfoStg(Long versionId, String user) throws DaoException {
        logger.debug("<updateControlFileInfoStg");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("versionId", versionId);
            parameters.put("user", user);
            this.mapper.updateControlFileInfoStg(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">updateControlFileInfoStg");
    }

    @Override
    public void uploadedVersion(Long versionId, String xml, Boolean hasErrorsInd, String user) throws DaoException {
        logger.debug("<uploadedVersion");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("versionId", versionId);
            parameters.put("xml", xml);
            parameters.put("hasErrorsInd", hasErrorsInd != null && hasErrorsInd ? "Y" : "N");
            parameters.put("user", user);
            this.mapper.uploadedVersion(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">uploadedVersion");
    }

    @Override
    public void startImport(Long versionId, String user) throws DaoException {
        logger.debug("<startImport");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("versionId", versionId);
            parameters.put("user", user);
            this.mapper.startImport(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">startImport");
    }

    @Override
    public void startUpload(Long versionId, String user) throws DaoException {
        logger.debug("<startUpload");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("versionId", versionId);
            parameters.put("user", user);
            this.mapper.startUpload(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">startUpload");
    }

    @Override
    public void performImport(Long versionId, String user) throws DaoException {
        logger.debug("<performImport");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("versionId", versionId);
            parameters.put("user", user);
            this.mapper.performImport(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">performImport");
    }

    @Override
    public void uploadFailure(Long versionId, String message, String user) throws DaoException {
        logger.debug("<uploadFailure");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("versionId", versionId);
            parameters.put("message", message);
            parameters.put("user", user);
            this.mapper.uploadFailure(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">uploadFailure");
    }

}
