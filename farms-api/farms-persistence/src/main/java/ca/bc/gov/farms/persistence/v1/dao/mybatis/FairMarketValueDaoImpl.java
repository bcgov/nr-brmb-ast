package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
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
    public List<FairMarketValueDto> fetchByProgramYear(Integer programYear) throws DaoException {
        logger.debug("<fetchByProgramYear");

        List<FairMarketValueDto> dtos = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("programYear", programYear);
            dtos = this.mapper.fetchBy(parameters);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchByProgramYear " + dtos);
        return dtos;
    }

}
