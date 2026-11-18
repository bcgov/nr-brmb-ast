package ca.bc.gov.farms.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.farms.data.assemblers.ScenarioBenefitResourceAssembler;
import ca.bc.gov.farms.data.mappers.ScenarioBenefitMapper;
import ca.bc.gov.farms.data.models.ScenarioBenefitListRsrc;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScenarioBenefitService {

    private final ScenarioBenefitMapper mapper;
    private final ScenarioBenefitResourceAssembler assembler;

    @Transactional(readOnly = true)
    public ScenarioBenefitListRsrc getScenarioBenefits(Integer participantPin, Integer programYear, Integer scenarioNumber) {
        return assembler.assemble(mapper.fetchScenarioBenefits(participantPin, programYear, scenarioNumber));
    }
}
