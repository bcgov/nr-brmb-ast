package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ConfigurationParameter;

public interface ConfigurationParameterService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    ConfigurationParameter getAllConfigurationParameters(FactoryContext factoryContext) throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    ConfigurationParameter getConfigurationParameter(Long configurationParameterId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    ConfigurationParameter createConfigurationParameter(ConfigurationParameter configurationParameter,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    ConfigurationParameter updateConfigurationParameter(Long configurationParameterId,
            ConfigurationParameter configurationParameter, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteConfigurationParameter(Long configurationParameterId)
            throws ServiceException, NotFoundException;
}
