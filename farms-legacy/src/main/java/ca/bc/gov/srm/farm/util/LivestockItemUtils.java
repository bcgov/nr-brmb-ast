package ca.bc.gov.srm.farm.util;

import ca.bc.gov.srm.farm.domain.LivestockItem;

public final class LivestockItemUtils {
  
  private LivestockItemUtils() {
    // private constructor
  }
  
  public static boolean hasOnlyZeroQuantities(LivestockItem item) {
    return !hasNonZeroQuantities(item);
  }
  
  public static boolean hasNonZeroQuantities(LivestockItem item) {
    return ProducedItemUtils.hasNonZeroQuantities(item);
  }
}