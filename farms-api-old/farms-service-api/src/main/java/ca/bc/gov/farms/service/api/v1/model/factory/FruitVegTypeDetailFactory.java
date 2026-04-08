package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetail;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetailList;
import ca.bc.gov.farms.persistence.v1.dto.FruitVegTypeDetailDto;

public interface FruitVegTypeDetailFactory {

    FruitVegTypeDetail getFruitVegTypeDetail(FruitVegTypeDetailDto dto, FactoryContext context);

    FruitVegTypeDetailList<? extends FruitVegTypeDetail> getFruitVegTypeDetailList(
            List<FruitVegTypeDetailDto> dtos, FactoryContext context);

    void updateFruitVegTypeDetail(FruitVegTypeDetailDto dto, FruitVegTypeDetail model);
}
