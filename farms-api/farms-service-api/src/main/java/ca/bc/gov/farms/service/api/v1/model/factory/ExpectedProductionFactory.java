package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ExpectedProduction;
import ca.bc.gov.farms.model.v1.ExpectedProductionList;
import ca.bc.gov.farms.persistence.v1.dto.ExpectedProductionDto;

public interface ExpectedProductionFactory {

    ExpectedProduction getExpectedProduction(ExpectedProductionDto dto, FactoryContext context);

    ExpectedProductionList<? extends ExpectedProduction> getExpectedProductionList(
            List<ExpectedProductionDto> dtos, FactoryContext context);

    void updateExpectedProduction(ExpectedProductionDto dto, ExpectedProduction model);
}
