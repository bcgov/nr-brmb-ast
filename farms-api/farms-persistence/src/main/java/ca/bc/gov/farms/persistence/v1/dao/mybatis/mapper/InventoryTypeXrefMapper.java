package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.InventoryTypeXrefDto;

public interface InventoryTypeXrefMapper {

    InventoryTypeXrefDto fetch(Map<String, Object> parameters);

    List<InventoryTypeXrefDto> fetchBy(Map<String, Object> parameters);

    int insertInventoryTypeXref(Map<String, Object> parameters);

    int updateInventoryTypeXref(Map<String, Object> parameters);

    int deleteInventoryTypeXref(Map<String, Object> parameters);
}
