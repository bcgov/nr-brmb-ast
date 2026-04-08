package ca.bc.gov.farms.service.api.v1.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ProductiveUnitCode;
import ca.bc.gov.farms.model.v1.ProductiveUnitCodeList;
import ca.bc.gov.farms.persistence.v1.dao.ProductiveUnitCodeDao;
import ca.bc.gov.farms.persistence.v1.dto.ProductiveUnitCodeDto;
import ca.bc.gov.farms.service.api.v1.ProductiveUnitCodeService;
import ca.bc.gov.farms.service.api.v1.model.factory.ProductiveUnitCodeFactory;

public class ProductiveUnitCodeServiceImpl implements ProductiveUnitCodeService {

    private static final Logger logger = LoggerFactory.getLogger(ProductiveUnitCodeServiceImpl.class);

    // factories
    private ProductiveUnitCodeFactory productiveUnitCodeFactory;

    // daos
    private ProductiveUnitCodeDao productiveUnitCodeDao;

    public void setProductiveUnitCodeFactory(ProductiveUnitCodeFactory productiveUnitCodeFactory) {
        this.productiveUnitCodeFactory = productiveUnitCodeFactory;
    }

    public void setProductiveUnitCodeDao(ProductiveUnitCodeDao productiveUnitCodeDao) {
        this.productiveUnitCodeDao = productiveUnitCodeDao;
    }

    @Override
    public ProductiveUnitCodeList<? extends ProductiveUnitCode> getAllProductiveUnitCodes(FactoryContext factoryContext)
            throws ServiceException {
        logger.debug("<getAllProductiveUnitCodes");

        ProductiveUnitCodeList<? extends ProductiveUnitCode> result = null;

        try {
            List<ProductiveUnitCodeDto> dtos = productiveUnitCodeDao.fetchAll();

            result = productiveUnitCodeFactory.getProductiveUnitCodeList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getAllProductiveUnitCodes");
        return result;
    }

}
