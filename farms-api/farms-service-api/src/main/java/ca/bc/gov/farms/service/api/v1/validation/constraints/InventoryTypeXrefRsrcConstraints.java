package ca.bc.gov.farms.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface InventoryTypeXrefRsrcConstraints {

    @NotBlank(message = Errors.MARKET_COMMODITY_IND_NOTBLANK, groups = InventoryTypeXrefRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.MARKET_COMMODITY_IND_SIZE, groups = InventoryTypeXrefRsrcConstraints.class)
    public String getMarketCommodityInd();

    @NotBlank(message = Errors.INVENTORY_ITEM_CODE_NOTBLANK, groups = InventoryTypeXrefRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = InventoryTypeXrefRsrcConstraints.class)
    public String getInventoryItemCode();

    @Size(min = 0, max = 10, message = Errors.INVENTORY_GROUP_CODE_SIZE, groups = InventoryTypeXrefRsrcConstraints.class)
    public String getInventoryGroupCode();

    @NotBlank(message = Errors.INVENTORY_CLASS_CODE_NOTBLANK, groups = InventoryTypeXrefRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.INVENTORY_CLASS_CODE_SIZE, groups = InventoryTypeXrefRsrcConstraints.class)
    public String getInventoryClassCode();

}
