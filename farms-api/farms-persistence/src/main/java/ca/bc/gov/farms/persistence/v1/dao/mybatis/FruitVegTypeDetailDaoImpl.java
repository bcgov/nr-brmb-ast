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
import ca.bc.gov.farms.persistence.v1.dao.FruitVegTypeDetailDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.FruitVegTypeDetailMapper;
import ca.bc.gov.farms.persistence.v1.dto.FruitVegTypeDetailDto;

public class FruitVegTypeDetailDaoImpl extends BaseDao implements FruitVegTypeDetailDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(FruitVegTypeDetailDaoImpl.class);

    @Autowired
    private FruitVegTypeDetailMapper mapper;

    @Override
    public FruitVegTypeDetailDto fetch(Long fruitVegTypeDetailId) throws DaoException {
        logger.debug("<fetch");

        FruitVegTypeDetailDto result = null;

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fruitVegTypeDetailId", fruitVegTypeDetailId);
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
    public List<FruitVegTypeDetailDto> fetchAll() throws DaoException {
        logger.debug("<fetchAll");

        List<FruitVegTypeDetailDto> dtos = null;

        try {
            dtos = this.mapper.fetchAll();
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchAll " + dtos);
        return dtos;
    }

    @Override
    public void insert(FruitVegTypeDetailDto dto, String userId) throws DaoException {
        logger.debug("<insert");

        Long fruitVegTypeDetailId = null;

        try {
            Map<String, Object> parameters = new HashMap<>();

            parameters.put("dto", dto);
            parameters.put("userId", userId);
            int count = this.mapper.insertFruitVegTypeDetail(parameters);

            if (count == 0) {
                throw new DaoException("Record not inserted: " + count);
            }

            fruitVegTypeDetailId = (Long) parameters.get("fruitVegTypeDetailId");
            dto.setFruitVegTypeDetailId(fruitVegTypeDetailId);
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">insert");
    }

    @Override
    public void update(FruitVegTypeDetailDto dto, String userId) throws DaoException, NotFoundDaoException {
        logger.debug("<update");

        if (dto.isDirty()) {
            try {
                Map<String, Object> parameters = new HashMap<>();

                parameters.put("dto", dto);
                parameters.put("userId", userId);
                int count = this.mapper.updateFruitVegTypeDetail(parameters);

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
    public void delete(Long fruitVegTypeDetailId) throws DaoException, NotFoundDaoException {
        logger.debug("<delete");

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fruitVegTypeDetailId", fruitVegTypeDetailId);
            int count = this.mapper.deleteFruitVegTypeDetail(parameters);

            if (count == 0) {
                throw new NotFoundDaoException("Record not deleted: " + count);
            }
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">delete");
    }

}
