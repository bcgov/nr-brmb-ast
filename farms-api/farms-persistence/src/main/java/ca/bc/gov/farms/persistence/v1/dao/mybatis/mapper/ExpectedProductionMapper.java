package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.ExpectedProductionDto;

public interface ExpectedProductionMapper {

    ExpectedProductionDto fetch(Map<String, Object> parameters);

    ExpectedProductionDto fetchByInventoryItemCode(Map<String, Object> parameters);

    List<ExpectedProductionDto> fetchAll();

    int insertExpectedProduction(Map<String, Object> parameters);

    int updateExpectedProduction(Map<String, Object> parameters);

    int deleteExpectedProduction(Map<String, Object> parameters);
}
