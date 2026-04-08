package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.InventoryTypeXrefEntity;

@Mapper
public interface InventoryTypeXrefMapper {

    InventoryTypeXrefEntity fetch(Long agristabilityCommodityXrefId);

    List<InventoryTypeXrefEntity> fetchByInventoryClassCode(String inventoryClassCode);

    int insertInventoryTypeXref(InventoryTypeXrefEntity dto, String userId);

    int updateInventoryTypeXref(InventoryTypeXrefEntity dto, String userId);

    int deleteInventoryTypeXref(Long agristabilityCommodityXrefId);
}
