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

/**
 * @author awilkinson
 */
public final class LineItemCodes {

  private LineItemCodes() {
    // empty constructor for utility class
  }

  public static final int CONTAINERS_AND_TWINE = 9661;
  public static final int COMMISSIONS_AND_LEVIES = 9836;
  
  public static final int CHICKENS = 366;
  public static final int CHICKEN_BROILER_PULLETS = 590;
  public static final int TURKEYS = 334;
  public static final int TURKEYS_NON_SUPPLY_MANAGED = 591;
  public static final int HOGS = 341;

  public static final int GRAIN_LINE_ITEM = 39;
  public static final String GRAIN_LINE_ITEM_DESCRIPTION = "Grain (pellets;screenings; silage)";
  
  public static final int FORAGE_LINE_ITEM = 264;
  public static final String FORAGE_LINE_ITEM_DESCRIPTION = "Forage (including pellets; silage)";

  public static final int OTHER_SPECIFY_9600 = 9600;
  public final static int OTHER_SPECIFY_9896 = 9896;
}
