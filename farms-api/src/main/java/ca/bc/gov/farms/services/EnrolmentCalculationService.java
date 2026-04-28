package ca.bc.gov.farms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.EnrolmentCalculationResourceAssembler;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationEntity;
import ca.bc.gov.farms.data.mappers.EnrolmentCalculationMapper;
import ca.bc.gov.farms.data.models.EnrolmentCalculationRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EnrolmentCalculationService {

    @Autowired
    private EnrolmentCalculationMapper enrolmentCalculationMapper;

    @Autowired
    private EnrolmentCalculationResourceAssembler enrolmentCalculationResourceAssembler;

    public EnrolmentCalculationRsrc getEnrolmentCalculation(Integer participantPin, Integer programYear)
            throws ServiceException, NotFoundException {

        if (participantPin == null || programYear == null) {
            throw new IllegalArgumentException("participantPin and programYear are required");
        }

        EnrolmentCalculationRsrc result = null;

        try {
            EnrolmentCalculationEntity entity = enrolmentCalculationMapper
                    .fetchLatestEnwByPinAndProgramYear(participantPin, programYear);

            if (entity == null) {
                throw new NotFoundException("Did not find an ENW enrolment calculation for participant PIN "
                        + participantPin + " and program year " + programYear);
            }

            result = enrolmentCalculationResourceAssembler.getEnrolmentCalculation(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }
}
