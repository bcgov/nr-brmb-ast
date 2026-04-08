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
import ca.bc.gov.farms.persistence.v1.dao.YearConfigurationParameterDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.YearConfigurationParameterMapper;
import ca.bc.gov.farms.persistence.v1.dto.YearConfigurationParameterDto;

public class YearConfigurationParameterDaoImpl extends BaseDao implements YearConfigurationParameterDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(YearConfigurationParameterDaoImpl.class);

    @Autowired
    private YearConfigurationParameterMapper mapper;

    @Override
    public YearConfigurationParameterDto fetch(Long yearConfigurationParameterId) throws DaoException {
        logger.debug("<fetch");

        YearConfigurationParameterDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("yearConfigurationParameterId", yearConfigurationParameterId);
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
    public List<YearConfigurationParameterDto> fetchAll()
            throws DaoException {
        logger.debug("<fetchAll");

        List<YearConfigurationParameterDto> dtos = null;

        try {
            dtos = this.mapper.fetchAll();
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchAll " + dtos);
        return dtos;
    }

    @Override
    public void insert(YearConfigurationParameterDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long yearConfigurationParameterId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertYearConfigurationParameter(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            yearConfigurationParameterId = (Long) parameters.get("yearConfigurationParameterId");
            dto.setYearConfigurationParameterId(yearConfigurationParameterId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(YearConfigurationParameterDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateYearConfigurationParameter(parameters);

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
    public void delete(Long yearConfigurationParameterId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("yearConfigurationParameterId", yearConfigurationParameterId);
            int count = this.mapper.deleteYearConfigurationParameter(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
