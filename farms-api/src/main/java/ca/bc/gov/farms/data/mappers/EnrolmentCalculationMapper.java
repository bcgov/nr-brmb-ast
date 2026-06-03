package ca.bc.gov.farms.data.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ca.bc.gov.farms.data.entities.EnrolmentCalculationEntity;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationMarginEntity;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationProductiveUnitEntity;
import ca.bc.gov.farms.data.entities.EnrolmentPartnerEntity;
import ca.bc.gov.farms.data.entities.EnrolmentPartnerSummaryEntity;

@Mapper
public interface EnrolmentCalculationMapper {

    EnrolmentCalculationEntity fetchLatestEnwByPinAndProgramYear(
            @Param("participantPin") Integer participantPin,
            @Param("programYear") Integer programYear);

    List<EnrolmentCalculationMarginEntity> fetchBenefitMargins(
            @Param("agristabilityScenarioId") Long agristabilityScenarioId);

    List<EnrolmentCalculationProductiveUnitEntity> fetchProductiveUnits(
            @Param("agristabilityScenarioId") Long agristabilityScenarioId,
            @Param("programYear") Integer programYear);

    List<EnrolmentPartnerEntity> fetchPartnersByPinAndProgramYear(
            @Param("participantPin") Integer participantPin,
            @Param("programYear") Integer programYear);

    EnrolmentPartnerSummaryEntity fetchPartnerSummaryByPinAndProgramYear(
            @Param("participantPin") Integer participantPin,
            @Param("programYear") Integer programYear);

    int insertScenarioEnrolment(
            @Param("dto") EnrolmentCalculationEntity entity,
            @Param("userId") String userId);

    int updateScenarioEnrolment(
            @Param("dto") EnrolmentCalculationEntity entity,
            @Param("userId") String userId);
}
