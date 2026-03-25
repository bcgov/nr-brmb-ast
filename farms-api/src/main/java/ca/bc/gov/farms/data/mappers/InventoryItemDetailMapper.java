package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.InventoryItemDetailEntity;

@Mapper
public interface InventoryItemDetailMapper {

    InventoryItemDetailEntity fetch(Long inventoryItemDetailId);

    List<InventoryItemDetailEntity> fetchByInventoryItemCode(String inventoryItemCode);

    int insertInventoryItemDetail(InventoryItemDetailEntity dto, String userId);

    int updateInventoryItemDetail(InventoryItemDetailEntity dto, String userId);

    int deleteInventoryItemDetail(Long inventoryItemDetailId);
}
