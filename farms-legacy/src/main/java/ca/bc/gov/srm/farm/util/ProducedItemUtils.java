package ca.bc.gov.srm.farm.util;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;

public final class ProducedItemUtils {
  
  private ProducedItemUtils() {
    // private constructor
  }
  
  public static boolean hasOnlyZeroQuantities(ProducedItem item) {
    return !hasNonZeroQuantities(item);
  }
  
  public static boolean hasNonZeroQuantities(ProducedItem item) {
    return (item.getTotalQuantityStart() != null && item.getTotalQuantityStart() != 0)
        || (item.getTotalQuantityEnd() != null && item.getTotalQuantityEnd() != 0)
        || (InventoryClassCodes.CROP.equals(item.getInventoryClassCode())
            && CropItemUtils.hasNonZeroQuantityProduced((CropItem) item));
  }
}