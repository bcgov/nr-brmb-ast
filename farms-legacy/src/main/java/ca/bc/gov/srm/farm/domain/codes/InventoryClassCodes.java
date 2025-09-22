/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

/**
 * InventoryClassCodes
 */
public final class InventoryClassCodes {

  /**  */
  private InventoryClassCodes() {
  }

  public static final String CROP = "1";

  public static final String LIVESTOCK = "2";

  public static final String INPUT = "3";
  
  public static final String PAYABLE = "5";
  
  public static final String RECEIVABLE = "4";
  
  public static final String UNKNOWN = "-1";

  public static boolean isAccrual(String inventoryClassCode) {
    return InventoryClassCodes.INPUT.equals(inventoryClassCode)
        || InventoryClassCodes.RECEIVABLE.equals(inventoryClassCode)
        || InventoryClassCodes.PAYABLE.equals(inventoryClassCode);
  }

}
