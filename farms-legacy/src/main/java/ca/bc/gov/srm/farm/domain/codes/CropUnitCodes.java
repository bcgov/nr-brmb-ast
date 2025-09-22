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
 * This class does not contain a full listing of crop unit codes.
 * We need a constant for the livestock crop unit code since it is always the same.
 * 
 * We need a few others for logic in the reasonability tests.
 * 
 * @author awilkinson
 * @created Mar 7, 2011
 */
public final class CropUnitCodes {

  /**  */
  private CropUnitCodes() {
  }

  /** Livestock always has this same crop unit code called: Head (Livestock) */
  public static final String HEAD_LIVESTOCK = "32";
  
  public static final String POUNDS = "1";
  public static final String TONNES = "2";
  public static final String DOZEN = "3";  
  public static final String KILOGRAM = "5";  
  public static final String GRAM = "6";  
  public static final String SMALL_BALES = "7";  
  public static final String LARGE_BALES = "8";
  public static final String CWT_HUNDREDWEIGHT = "16";  
  public static final String HIVES = "99";  
  
  public static final String POUNDS_DESCRIPTION = "Pounds";
  public static final String TONNES_DESCRIPTION = "Tonnes";
  public static final String KILOGRAM_DESCRIPTION = "Kilogram";
  public static final String GRAM_DESCRIPTION = "Gram";
  public static final String SMALL_BALES_DESCRIPTION = "Small Bales";
  public static final String LARGE_BALES_DESCRIPTION = "Large Bales";
  public static final String CWT_HUNDREDWEIGHT_DESCRIPTION = "CWT";
  
  
  public static String getLivestockUnitCode(String inventoryItemCode) {

    switch (inventoryItemCode) {
    case "7603":        // Bees
    case "7600":        // Beeswax
    case "7604":        // Honey
    case "7602":        // Beeswax Produced
    case "7614":        // Honey Produced
      return POUNDS;
    case "7606":        // Honey Bees
    case "7608":        // Honey Bees Colony
    case "7610":        // Honey Bees Package
    case "7612":        // Honey Bees; Producing (Hives)
      return HIVES;
    case "7663":        // Eggs for Hatching
    case "7664":        // Eggs for Consumption 
    case "7665":        // Eggs for Consumption; Organic
      return DOZEN;
    default:
      return HEAD_LIVESTOCK;
    }
  }
}