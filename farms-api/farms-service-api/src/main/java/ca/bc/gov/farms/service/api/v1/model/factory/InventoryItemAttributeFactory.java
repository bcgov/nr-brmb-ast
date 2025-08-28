package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.InventoryItemAttribute;
import ca.bc.gov.farms.model.v1.InventoryItemAttributeList;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemAttributeDto;

public interface InventoryItemAttributeFactory {

    InventoryItemAttribute getInventoryItemAttribute(InventoryItemAttributeDto dto, FactoryContext context);

    InventoryItemAttributeList<? extends InventoryItemAttribute> getInventoryItemAttributeList(
            List<InventoryItemAttributeDto> dtos, FactoryContext context);

    void updateInventoryItemAttribute(InventoryItemAttributeDto dto, InventoryItemAttribute model);
}
