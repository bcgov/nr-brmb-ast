/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

import java.util.Arrays;
import java.util.List;

/**
 * @author awilkinson
 * @created Sep 21, 2021
 */
public final class SectorDetailCodes {

  private SectorDetailCodes() {
    // private constructor
  }
  
  /** Mixed Vegetables */
  public static final String MIX_VEG = "MIX_VEG";
  
  /** Vegetables - Greenhouse */
  /** Mixed Other Crops */
  public static final String MIX_OTH_CR = "MIX_OTH_CR";
  
  /** Mixed Supply Managed */
  public static final String MIX_SM = "MIX_SM";
  
  /** Mixed Cattle */
  public static final String MIX_CATTLE = "MIX_CATTLE";
  
  /** Mixed Livestock */
  public static final String MIX_LIVE = "MIX_LIVE";
  /** Mixed Crops - G & O */
  public static final String MIX_CR_G_O = "MIX_CR_G_O";
  
  /** Mixed Fruit */
  public static final String MIX_FRUIT = "MIX_FRUIT";
  
  /** Mixed Farm */
  public static final String MIX_FARM = "MIX_FARM";
  
  /** No Income */
  public static final String NO_INCOME = "NO_INCOME";

  private static List<String> MIXED_CODES = Arrays.asList(new String[] {
      MIX_VEG,
      MIX_OTH_CR,
      MIX_SM,
      MIX_CATTLE,
      MIX_LIVE,
      MIX_CR_G_O,
      MIX_FRUIT,
      MIX_FARM,
      NO_INCOME,
  });
  
  public static boolean isMixed(String sectorDetailCode) {
    return MIXED_CODES.contains(sectorDetailCode);
  }
}
