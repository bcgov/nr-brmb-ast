package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.FairMarketValueDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.FairMarketValueMapper;
import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;

public class FairMarketValueDaoImpl extends BaseDao implements FairMarketValueDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(FairMarketValueDaoImpl.class);

    @Autowired
    private FairMarketValueMapper mapper;

    @Override
    public FairMarketValueDto fetch(Integer programYear, String fairMarketValueId) throws DaoException {
        logger.debug("<fetch");

        FairMarketValueDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("programYear", programYear);
            parameters.put("fairMarketValueId", fairMarketValueId);
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
    public List<FairMarketValueDto> fetchByProgramYear(Integer programYear) throws DaoException {
        logger.debug("<fetchByProgramYear");

        List<FairMarketValueDto> dtos = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("programYear", programYear);
            dtos = this.mapper.fetchByProgramYear(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchByProgramYear " + dtos);
        return dtos;
    }

    @Override
    public List<FairMarketValueDto> fetchBy(Integer programYear, String inventoryItemCode, String municipalityCode,
            String cropUnitCode) throws DaoException {
        logger.debug("<fetchBy");

        List<FairMarketValueDto> dtos = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("programYear", programYear);
            parameters.put("inventoryItemCode", inventoryItemCode);
            parameters.put("municipalityCode", municipalityCode);
            parameters.put("cropUnitCode", cropUnitCode);
            dtos = this.mapper.fetchBy(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchBy " + dtos);
        return dtos;
    }

    @Override
    public void insert(FairMarketValueDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("dto", dto);
            parameters.put("userId", userId);

            BigDecimal[] averagePrices = { dto.getPeriod01Price(), dto.getPeriod02Price(), dto.getPeriod03Price(),
                    dto.getPeriod04Price(), dto.getPeriod05Price(), dto.getPeriod06Price(),
                    dto.getPeriod07Price(), dto.getPeriod08Price(), dto.getPeriod09Price(),
                    dto.getPeriod10Price(), dto.getPeriod11Price(), dto.getPeriod12Price() };
            BigDecimal[] percentVariances = { dto.getPeriod01Variance(), dto.getPeriod02Variance(),
                    dto.getPeriod03Variance(), dto.getPeriod04Variance(), dto.getPeriod05Variance(),
                    dto.getPeriod06Variance(), dto.getPeriod07Variance(), dto.getPeriod08Variance(),
                    dto.getPeriod09Variance(), dto.getPeriod10Variance(), dto.getPeriod11Variance(),
                    dto.getPeriod12Variance() };

            for (int period = 1; period <= 12; period++) {
                parameters.put("period", period);
                parameters.put("averagePrice", averagePrices[period - 1]);
                parameters.put("percentVariance", percentVariances[period - 1]);
                int count = this.mapper.insertFairMarketValue(parameters);

                if (count == 0) {
                    throw new DaoException("Record not inserted: " + count);
                }
            }

            String fairMarketValueId = dto.getProgramYear() + "_" + dto.getInventoryItemCode() + "_"
                    + dto.getMunicipalityCode() + "_" + dto.getCropUnitCode();
            dto.setFairMarketValueId(fairMarketValueId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert ");
    }

    @Override
    public void update(FairMarketValueDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("fairMarketValueId", dto.getFairMarketValueId());
                parameters.put("urlId", dto.getUrlId());
                parameters.put("userId", userId);

                BigDecimal[] averagePrices = { dto.getPeriod01Price(), dto.getPeriod02Price(), dto.getPeriod03Price(),
                        dto.getPeriod04Price(), dto.getPeriod05Price(), dto.getPeriod06Price(),
                        dto.getPeriod07Price(), dto.getPeriod08Price(), dto.getPeriod09Price(),
                        dto.getPeriod10Price(), dto.getPeriod11Price(), dto.getPeriod12Price() };
                BigDecimal[] percentVariances = { dto.getPeriod01Variance(), dto.getPeriod02Variance(),
                        dto.getPeriod03Variance(), dto.getPeriod04Variance(), dto.getPeriod05Variance(),
                        dto.getPeriod06Variance(), dto.getPeriod07Variance(), dto.getPeriod08Variance(),
                        dto.getPeriod09Variance(), dto.getPeriod10Variance(), dto.getPeriod11Variance(),
                        dto.getPeriod12Variance() };

                for (int period = 1; period <= 12; period++) {
                    parameters.put("period", period);
                    parameters.put("averagePrice", averagePrices[period - 1]);
                    parameters.put("percentVariance", percentVariances[period - 1]);
                    int count = this.mapper.updateFairMarketValue(parameters);

                    if (count == 0) {
                        throw new NotFoundDaoException("Record not updated: " + count);
                    }
                }
            } catch (RuntimeException e) {
                handleException(e);
            }
        }

        logger.debug(">update");
    }

    @Override
    public void delete(Integer programYear, String fairMarketValueId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("programYear", programYear);
            parameters.put("fairMarketValueId", fairMarketValueId);
            int count = this.mapper.deleteFairMarketValue(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
