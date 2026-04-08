package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.InventoryTypeXref;
import ca.bc.gov.farms.model.v1.InventoryTypeXrefList;
import ca.bc.gov.farms.persistence.v1.dto.InventoryTypeXrefDto;

public interface InventoryTypeXrefFactory {

    InventoryTypeXref getInventoryTypeXref(InventoryTypeXrefDto dto, FactoryContext context);

    InventoryTypeXrefList<? extends InventoryTypeXref> getInventoryTypeXrefList(
            List<InventoryTypeXrefDto> dtos, FactoryContext context);

    void updateInventoryTypeXref(InventoryTypeXrefDto dto, InventoryTypeXref model);
}
