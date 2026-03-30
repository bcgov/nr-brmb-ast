package ca.bc.gov.farms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.ExpectedProductionResourceAssembler;
import ca.bc.gov.farms.data.entities.ExpectedProductionEntity;
import ca.bc.gov.farms.data.mappers.ExpectedProductionMapper;
import ca.bc.gov.farms.data.models.ExpectedProductionListModel;
import ca.bc.gov.farms.data.models.ExpectedProductionModel;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExpectedProductionService {

    @Autowired
    private ExpectedProductionMapper expectedProductionMapper;

    @Autowired
    private ExpectedProductionResourceAssembler expectedProductionResourceAssembler;

    @Autowired
    private Validator validator;

    public ExpectedProductionListModel getAllExpectedProductions() throws ServiceException {

        ExpectedProductionListModel result = null;

        try {
            List<ExpectedProductionEntity> entities = expectedProductionMapper.fetchAll();

            result = expectedProductionResourceAssembler.getExpectedProductionList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public ExpectedProductionListModel getExpectedProductionByInventoryItemCode(String inventoryItemCode)
            throws ServiceException {

        ExpectedProductionListModel result = null;

        try {
            List<ExpectedProductionEntity> entities = expectedProductionMapper
                    .fetchByInventoryItemCode(inventoryItemCode);

            result = expectedProductionResourceAssembler.getExpectedProductionList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public ExpectedProductionModel getExpectedProduction(Long expectedProductionId)
            throws ServiceException, NotFoundException {

        ExpectedProductionModel result = null;

        try {
            ExpectedProductionEntity entity = expectedProductionMapper.fetch(expectedProductionId);

            if (entity == null) {
                throw new NotFoundException("Did not find the expected production: " + expectedProductionId);
            }

            result = expectedProductionResourceAssembler.getExpectedProduction(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }
}
