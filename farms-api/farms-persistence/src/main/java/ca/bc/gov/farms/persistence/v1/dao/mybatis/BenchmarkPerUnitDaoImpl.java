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
import ca.bc.gov.farms.persistence.v1.dao.BenchmarkPerUnitDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.BenchmarkPerUnitMapper;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;

public class BenchmarkPerUnitDaoImpl extends BaseDao implements BenchmarkPerUnitDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkPerUnitDaoImpl.class);

    @Autowired
    private BenchmarkPerUnitMapper mapper;

    @Override
    public BenchmarkPerUnitDto fetch(Long benchmarkPerUnitId) throws DaoException {
        logger.debug("<fetch");

        BenchmarkPerUnitDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("benchmarkPerUnitId", benchmarkPerUnitId);
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
    public List<BenchmarkPerUnitDto> fetchByProgramYear(Integer programYear) throws DaoException {
        logger.debug("<fetchByProgramYear");

        List<BenchmarkPerUnitDto> dtos = null;

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

    @Override
    public void insert(BenchmarkPerUnitDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long benchmarkPerUnitId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insert(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            benchmarkPerUnitId = (Long) parameters.get("benchmarkPerUnitId");
            dto.setBenchmarkPerUnitId(benchmarkPerUnitId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(BenchmarkPerUnitDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.update(parameters);

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
    public void delete(Long benchmarkPerUnitId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("benchmarkPerUnitId", benchmarkPerUnitId);
            int count = this.mapper.delete(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
