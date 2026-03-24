package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.InventoryItemDetailDto;

public interface InventoryItemDetailMapper {

    InventoryItemDetailDto fetch(Map<String, Object> parameters);

    List<InventoryItemDetailDto> fetchBy(Map<String, Object> parameters);

    int insertInventoryItemDetail(Map<String, Object> parameters);

    int updateInventoryItemDetail(Map<String, Object> parameters);

    int deleteInventoryItemDetail(Map<String, Object> parameters);
}
