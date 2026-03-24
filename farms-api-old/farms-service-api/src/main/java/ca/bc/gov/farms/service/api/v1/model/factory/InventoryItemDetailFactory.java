package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.InventoryItemDetail;
import ca.bc.gov.farms.model.v1.InventoryItemDetailList;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemDetailDto;

public interface InventoryItemDetailFactory {

    InventoryItemDetail getInventoryItemDetail(InventoryItemDetailDto dto, FactoryContext context);

    InventoryItemDetailList<? extends InventoryItemDetail> getInventoryItemDetailList(
            List<InventoryItemDetailDto> dtos, FactoryContext context);

    void updateInventoryItemDetail(InventoryItemDetailDto dto, InventoryItemDetail model);
}
