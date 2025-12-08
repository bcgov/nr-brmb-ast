package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ProductiveUnitCode;
import ca.bc.gov.farms.model.v1.ProductiveUnitCodeList;
import ca.bc.gov.farms.persistence.v1.dto.ProductiveUnitCodeDto;

public interface ProductiveUnitCodeFactory {

    ProductiveUnitCodeList<? extends ProductiveUnitCode> getProductiveUnitCodeList(List<ProductiveUnitCodeDto> dtos,
            FactoryContext context);
}
