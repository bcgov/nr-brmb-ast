package ca.bc.gov.farms.data.mappers;

import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.InventoryItemAttributeEntity;

@Mapper
public interface InventoryItemAttributeMapper {

    InventoryItemAttributeEntity fetch(Long inventoryItemAttributeId);

    InventoryItemAttributeEntity fetchByInventoryItemCode(String inventoryItemCode);

    int insertInventoryItemAttribute(InventoryItemAttributeEntity dto, String userId);

    int updateInventoryItemAttribute(InventoryItemAttributeEntity dto, String userId);

    int deleteInventoryItemAttribute(Long inventoryItemAttributeId);
}
