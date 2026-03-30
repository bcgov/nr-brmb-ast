package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.ExpectedProductionEntity;

@Mapper
public interface ExpectedProductionMapper {

    ExpectedProductionEntity fetch(Long expectedProductionId);

    List<ExpectedProductionEntity> fetchByInventoryItemCode(String inventoryItemCode);

    List<ExpectedProductionEntity> fetchAll();

    int insertExpectedProduction(ExpectedProductionEntity dto, String userId);

    int updateExpectedProduction(ExpectedProductionEntity dto, String userId);

    int deleteExpectedProduction(Long expectedProductionId);
}
