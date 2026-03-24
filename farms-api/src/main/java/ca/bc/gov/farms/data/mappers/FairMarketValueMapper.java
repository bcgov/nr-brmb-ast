package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.FairMarketValueEntity;

@Mapper
public interface FairMarketValueMapper {

    FairMarketValueEntity fetch(Integer programYear, String fairMarketValueId);

    List<FairMarketValueEntity> fetchByProgramYear(Integer programYear);

    List<FairMarketValueEntity> fetchBy(Integer programYear, String inventoryItemCode, String municipalityCode,
            String cropUnitCode);

    int insertFairMarketValue(FairMarketValueEntity dto, String userId);

    int updateFairMarketValue(FairMarketValueEntity dto, String userId);

    int deleteFairMarketValue(Integer programYear, String fairMarketValueId);
}
