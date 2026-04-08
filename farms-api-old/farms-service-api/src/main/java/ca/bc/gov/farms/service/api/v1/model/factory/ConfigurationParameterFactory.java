package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ConfigurationParameter;
import ca.bc.gov.farms.model.v1.ConfigurationParameterList;
import ca.bc.gov.farms.persistence.v1.dto.ConfigurationParameterDto;

public interface ConfigurationParameterFactory {

    ConfigurationParameter getConfigurationParameter(ConfigurationParameterDto dto, FactoryContext context);

    ConfigurationParameterList<? extends ConfigurationParameter> getConfigurationParameterList(
            List<ConfigurationParameterDto> dtos, FactoryContext context);

    void updateConfigurationParameter(ConfigurationParameterDto dto, ConfigurationParameter model);
}
