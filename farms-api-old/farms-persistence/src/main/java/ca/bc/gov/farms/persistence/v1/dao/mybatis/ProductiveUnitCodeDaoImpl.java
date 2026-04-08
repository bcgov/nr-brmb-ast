package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ProductiveUnitCodeDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper.ProductiveUnitCodeMapper;
import ca.bc.gov.farms.persistence.v1.dto.ProductiveUnitCodeDto;

public class ProductiveUnitCodeDaoImpl extends BaseDao implements ProductiveUnitCodeDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ProductiveUnitCodeDaoImpl.class);

    @Autowired
    private ProductiveUnitCodeMapper mapper;

    @Override
    public List<ProductiveUnitCodeDto> fetchAll() throws DaoException {
        logger.debug("<fetchAll");

        List<ProductiveUnitCodeDto> dtos = null;

        try {
            dtos = this.mapper.fetchAll();
        } catch (RuntimeException e) {
            handleException(e);
        }

        logger.debug(">fetchAll " + dtos);
        return dtos;
    }

}
