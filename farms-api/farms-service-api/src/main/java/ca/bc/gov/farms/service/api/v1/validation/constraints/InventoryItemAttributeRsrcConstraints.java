package ca.bc.gov.farms.service.api.v1.validation.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface InventoryItemAttributeRsrcConstraints {

    @NotBlank(message = Errors.INVENTORY_ITEM_CODE_NOTBLANK, groups = InventoryItemAttributeRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = InventoryItemAttributeRsrcConstraints.class)
    public String getInventoryItemCode();

    @Size(min = 0, max = 10, message = Errors.ROLLUP_INVENTORY_ITEM_CODE_SIZE, groups = InventoryItemAttributeRsrcConstraints.class)
    public String getRollupInventoryItemCode();

}
