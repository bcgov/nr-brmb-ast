package ca.bc.gov.farms.data.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import ca.bc.gov.farms.data.entities.EnrolmentCalculationEntity;

@Mapper
public interface EnrolmentCalculationMapper {

    EnrolmentCalculationEntity fetchLatestEnwByPinAndProgramYear(
            @Param("participantPin") Integer participantPin,
            @Param("programYear") Integer programYear);
}
