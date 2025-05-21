package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.FairMarketValue;
import ca.bc.gov.farms.model.v1.FairMarketValueList;
import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;

public interface FairMarketValueFactory {

    FairMarketValue getFairMarketValue(FairMarketValueDto dto, FactoryContext context);

    FairMarketValueList<? extends FairMarketValue> getFairMarketValueList(
            List<FairMarketValueDto> dtos, FactoryContext context);

    void updateFairMarketValue(FairMarketValueDto dto, FairMarketValue model);
}
