package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.VersionDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.VersionMapper;

public class VersionDaoImpl extends BaseDao implements VersionDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(VersionDaoImpl.class);

    @Autowired
    private VersionMapper mapper;

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

}
