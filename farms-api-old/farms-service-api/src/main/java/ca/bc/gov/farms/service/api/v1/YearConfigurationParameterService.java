package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.YearConfigurationParameter;
import ca.bc.gov.farms.model.v1.YearConfigurationParameterList;

public interface YearConfigurationParameterService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    YearConfigurationParameterList<? extends YearConfigurationParameter> getAllYearConfigurationParameters(
            FactoryContext factoryContext) throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    YearConfigurationParameter getYearConfigurationParameter(Long yearConfigurationParameterId,
            FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    YearConfigurationParameter createYearConfigurationParameter(YearConfigurationParameter yearConfigurationParameter,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    YearConfigurationParameter updateYearConfigurationParameter(Long yearConfigurationParameterId,
            YearConfigurationParameter yearConfigurationParameter, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteYearConfigurationParameter(Long yearConfigurationParameterId)
            throws ServiceException, NotFoundException;
}
