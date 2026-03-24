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
import ca.bc.gov.farms.persistence.v1.dao.MarketRatePremiumDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.MarketRatePremiumMapper;
import ca.bc.gov.farms.persistence.v1.dto.MarketRatePremiumDto;

public class MarketRatePremiumDaoImpl extends BaseDao implements MarketRatePremiumDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(MarketRatePremiumDaoImpl.class);

    @Autowired
    private MarketRatePremiumMapper mapper;

    @Override
    public MarketRatePremiumDto fetch(Long marketRatePremiumId) throws DaoException {
        logger.debug("<fetch");

        MarketRatePremiumDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("marketRatePremiumId", marketRatePremiumId);
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
    public List<MarketRatePremiumDto> fetchAll()
            throws DaoException {
        logger.debug("<fetchAll");

        List<MarketRatePremiumDto> dtos = null;

        try {
            dtos = this.mapper.fetchAll();
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchAll " + dtos);
        return dtos;
    }

    @Override
    public void insert(MarketRatePremiumDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long marketRatePremiumId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertMarketRatePremium(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            marketRatePremiumId = (Long) parameters.get("marketRatePremiumId");
            dto.setMarketRatePremiumId(marketRatePremiumId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(MarketRatePremiumDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateMarketRatePremium(parameters);

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
    public void delete(Long marketRatePremiumId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("marketRatePremiumId", marketRatePremiumId);
            int count = this.mapper.deleteMarketRatePremium(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
