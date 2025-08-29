package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.InventoryItemAttributeDto;

public interface InventoryItemAttributeMapper {

    InventoryItemAttributeDto fetch(Map<String, Object> parameters);

    InventoryItemAttributeDto fetchByInventoryItemCode(Map<String, Object> parameters);

    int insertInventoryItemAttribute(Map<String, Object> parameters);

    int updateInventoryItemAttribute(Map<String, Object> parameters);

    int deleteInventoryItemAttribute(Map<String, Object> parameters);
}
