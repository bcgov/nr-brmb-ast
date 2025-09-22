package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ConfigurationParameterDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.ConfigurationParameterMapper;
import ca.bc.gov.farms.persistence.v1.dto.ConfigurationParameterDto;

public class ConfigurationParameterDaoImpl extends BaseDao implements ConfigurationParameterDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationParameterDaoImpl.class);

    @Autowired
    private ConfigurationParameterMapper mapper;

    @Override
    public ConfigurationParameterDto fetch(Long configurationParameterId) throws DaoException {
        logger.debug("<fetch");

        ConfigurationParameterDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("configurationParameterId", configurationParameterId);
            result = this.mapper.fetch(parameters);

            if (result != null) {
                result.resetDirty();
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetch " + result);
        return result;
    }

    @Override
    public List<ConfigurationParameterDto> fetchAll()
            throws DaoException {
        logger.debug("<fetchAll");

        List<ConfigurationParameterDto> dtos = null;

        try {
            dtos = this.mapper.fetchAll();
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchAll " + dtos);
        return dtos;
    }

    @Override
    public void insert(ConfigurationParameterDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long configurationParameterId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertConfigurationParameter(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            configurationParameterId = (Long) parameters.get("configurationParameterId");
            dto.setConfigurationParameterId(configurationParameterId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(ConfigurationParameterDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateConfigurationParameter(parameters);

                if (count == 0) {
                    throw new NotFoundDaoException("Record not updated: " + count);
                }
            } catch (RuntimeException e) {
                handleException(e);
            }
        }

        logger.debug(">update");
    }

    @Override
    public void delete(Long configurationParameterId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("configurationParameterId", configurationParameterId);
            int count = this.mapper.deleteConfigurationParameter(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
