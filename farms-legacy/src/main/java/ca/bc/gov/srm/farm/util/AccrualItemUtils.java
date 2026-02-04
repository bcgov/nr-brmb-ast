package ca.bc.gov.srm.farm.util;

import ca.bc.gov.srm.farm.domain.InventoryItem;

public final class AccrualItemUtils {
  
  private AccrualItemUtils() {
    // private constructor
  }
  
  public static boolean hasOnlyZeroAmounts(InventoryItem item) {
    return !hasNonZeroAmounts(item);
  }
  
  public static boolean hasNonZeroAmounts(InventoryItem item) {
    return (item.getTotalStartOfYearAmount() != null && item.getTotalStartOfYearAmount() != 0)
        || (item.getTotalEndOfYearAmount() != null && item.getTotalEndOfYearAmount() != 0);
  }
}