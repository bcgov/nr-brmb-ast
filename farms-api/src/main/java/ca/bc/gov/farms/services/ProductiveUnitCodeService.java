package ca.bc.gov.farms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.ProductiveUnitCodeResourceAssembler;
import ca.bc.gov.farms.data.entities.ProductiveUnitCodeEntity;
import ca.bc.gov.farms.data.mappers.ProductiveUnitCodeMapper;
import ca.bc.gov.farms.data.models.ProductiveUnitCodeListRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductiveUnitCodeService {

    @Autowired
    private ProductiveUnitCodeMapper productiveUnitCodeMapper;

    @Autowired
    private ProductiveUnitCodeResourceAssembler productiveUnitCodeResourceAssembler;

    public ProductiveUnitCodeListRsrc getAllProductiveUnitCodes() throws ServiceException {

        ProductiveUnitCodeListRsrc result = null;

        try {
            List<ProductiveUnitCodeEntity> entities = productiveUnitCodeMapper.fetchAll();

            result = productiveUnitCodeResourceAssembler.getProductiveUnitCodeList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }
}
