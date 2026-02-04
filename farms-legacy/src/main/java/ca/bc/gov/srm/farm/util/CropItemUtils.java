package ca.bc.gov.srm.farm.util;

import ca.bc.gov.srm.farm.domain.CropItem;

public final class CropItemUtils {
  
  private CropItemUtils() {
    // private constructor
  }
  
  public static boolean hasOnlyZeroQuantities(CropItem item) {
    return !hasNonZeroQuantities(item);
  }
  
  public static boolean hasNonZeroQuantities(CropItem item) {
    return ProducedItemUtils.hasNonZeroQuantities(item);
  }

  public static boolean hasNonZeroQuantityProduced(CropItem item) {
    return item.getTotalQuantityProduced() != null && item.getTotalQuantityProduced() != 0;
  }
}