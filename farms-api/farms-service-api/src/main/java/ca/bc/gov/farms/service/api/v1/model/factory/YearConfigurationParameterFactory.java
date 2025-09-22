package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.YearConfigurationParameter;
import ca.bc.gov.farms.model.v1.YearConfigurationParameterList;
import ca.bc.gov.farms.persistence.v1.dto.YearConfigurationParameterDto;

public interface YearConfigurationParameterFactory {

    YearConfigurationParameter getYearConfigurationParameter(YearConfigurationParameterDto dto, FactoryContext context);

    YearConfigurationParameterList<? extends YearConfigurationParameter> getYearConfigurationParameterList(
            List<YearConfigurationParameterDto> dtos, FactoryContext context);

    void updateYearConfigurationParameter(YearConfigurationParameterDto dto, YearConfigurationParameter model);
}
