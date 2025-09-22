/**
 * Copyright (c) 2019,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public final class InventoryItemCodes {

  private InventoryItemCodes() {
    // empty constructor for utility class
  }

  public static final String APPLES_GALA_5_PLUS_YEARS = "4826";
  public static final String POU_BR_CHICKEN = "7681";
  
  public static final String TURKEYS_6KG_TO_8KG = "7863";
  public static final String TURKEYS_6KG_TO_8KG_DESCRIPTION = "Turkeys (over 6.2 kg up to 8.5 kg)";
  
  public static final String CHICKEN_EGGS_HATCH = "7663";
  public static final String CHICKEN_EGGS_CONSUMP = "7664";
  
  
  public static final String HOGS_BOARS_BREEDING = "8752";
  public static final String HOGS_BOARS_BREEDING_DESCRIPTION = "Hogs; Breeding; Boars";
  
  public static final String HOGS_PUREBRED_BOARS_BREEDING = "8774";
  public static final String HOGS_PUREBRED_BOARS_BREEDING_DESCRIPTION = "Purebred Hogs; Breeding; Boars";
  
  public static final String HOGS_SOWS_BREEDING = "8754";
  public static final String HOGS_SOWS_BREEDING_DESCRIPTION = "Hogs; Breeding; Sows";
  
  public static final String HOGS_PUREBRED_SOWS_BREEDING = "8776";
  public static final String HOGS_PUREBRED_SOWS_BREEDING_DESCRIPTION = "Purebred Hogs; Breeding; Sows";
  
  public static final String HOGS_FEEDER_BIRTH = "8763";
  public static final String HOGS_FEEDER_BIRTH_DESCRIPTION = "Hogs; Feeder; Birth - 18 lbs.";
  
  // Multistage Commodities - Generic Codes:
  public static final String APPLES = "5030";
  public static final String APPLES_ORGANIC = "5031";
  public static final String BLUEBERRIES = "5002";
  public static final String CHERRIES = "5040";
  public static final String CRANBERRIES = "5006";
  public static final String GRAPES = "5014";
  
  public static final String OTHER_SPECIFY_9896 = "9896";
  
  public static boolean isGenericMultistageCommodityCode(String inventoryItemCode) {
    boolean result = 
        StringUtils.isOneOf(inventoryItemCode,
        APPLES,
        APPLES_ORGANIC,
        BLUEBERRIES,
        CHERRIES,
        CRANBERRIES,
        GRAPES);
    return result;
  }

}
