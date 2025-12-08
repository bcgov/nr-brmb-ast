/**
 * Copyright (c) 2020,
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
 * This class does not contain a full listing of Fruit and Vegetable type codes.
 * We need these for logic in the reasonability tests.
 * 
 * @author awilkinson
 * @created Mar 7, 2011
 */
public final class CommodityTypeCodes {

  private CommodityTypeCodes() {
  }

  public static final String GRAIN = "GRAIN";
  public static final String FORAGE = "FORAGE";
  public static final String FORAGE_SEED = "FORAGESEED";
  public static final String FRUIT_VEG = "FRUIT_VEG";
  
  public static final String CATTLE = "CATTLE";
  public static final String CATTLE_DESCRIPTION = "Cattle";
  
  public static final String NURSERY = "NURSERY";
  public static final String HOG = "HOG";
  public static final String POULTRY_BROILER_CHICKEN = "POU_BR_CHK";
  public static final String POULTRY_BROILER_TURKEY = "POU_BR_TUR";
  public static final String POULTRY_EGGS_CONSUMPTION = "POU_EG_CON";
  public static final String POULTRY_EGGS_HATCHING = "POU_EG_HAT";
  public static final String NULL = null; // for ease of reading unit tests

  public static String getCommodityTypeForStructureGroup(String structureGroupCode) {
    String commodityTypeCode;
    
    switch(structureGroupCode) {
    case StructureGroupCodes.CHICKEN_EGGS_CONSUMPTION:
      commodityTypeCode = POULTRY_EGGS_CONSUMPTION;
      break;
    case StructureGroupCodes.CHICKEN_EGGS_HATCH:
      commodityTypeCode = POULTRY_EGGS_HATCHING;
      break;
    case StructureGroupCodes.CHICKEN_BROILERS:
      commodityTypeCode = POULTRY_BROILER_CHICKEN;
      break;
    case StructureGroupCodes.TURKEY_BROILERS:
      commodityTypeCode = POULTRY_BROILER_TURKEY;
      break;
    case StructureGroupCodes.HOGS_FARROW_TO_FINISH:
      commodityTypeCode = HOG;
      break;
    case StructureGroupCodes.HOGS_FEEDER:
      commodityTypeCode = HOG;
      break;
    default:
      commodityTypeCode = null;
    }
    
    return commodityTypeCode;
  }
}