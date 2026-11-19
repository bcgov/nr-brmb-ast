package ca.bc.gov.farms.data.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ca.bc.gov.farms.data.entities.ScenarioBenefitEntity;

@Mapper
public interface ScenarioBenefitMapper {

    List<ScenarioBenefitEntity> fetchScenarioBenefits(
            @Param("participantPin") Integer participantPin,
            @Param("programYear") Integer programYear,
            @Param("scenarioNumber") Integer scenarioNumber);
}
