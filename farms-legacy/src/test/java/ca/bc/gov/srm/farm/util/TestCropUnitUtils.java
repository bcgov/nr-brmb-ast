/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;

/**
 * @author awilkinson
 */
public class TestCropUnitUtils {

  private static Map<String, CropUnitConversion> conversions;
  
  @BeforeAll
  protected static void setUp() {
    conversions = new HashMap<>();
    
    {
      CropUnitConversion cuc = new CropUnitConversion();
      conversions.put("5560", cuc);
      cuc.setDefaultCropUnitCode("2");
      cuc.setDefaultCropUnitCodeDescription("Tonnes");
      cuc.setInventoryItemCode("5560");
      cuc.setInventoryItemCodeDescription("Alfalfa Dehy");
      {
        CropUnitConversionItem item = new CropUnitConversionItem();
        item.setTargetCropUnitCode("1");
        item.setTargetCropUnitCodeDescription("Pounds");
        item.setConversionFactor(BigDecimal.valueOf(2204.6226));
        cuc.getConversionItems().add(item);
      }
      {
        CropUnitConversionItem item = new CropUnitConversionItem();
        item.setTargetCropUnitCode("7");
        item.setTargetCropUnitCodeDescription("Small Bales");
        item.setConversionFactor(BigDecimal.valueOf(36.74371));
        cuc.getConversionItems().add(item);
      }
      {
        CropUnitConversionItem item = new CropUnitConversionItem();
        item.setTargetCropUnitCode("8");
        item.setTargetCropUnitCodeDescription("Large Bales");
        item.setConversionFactor(BigDecimal.valueOf(2.2046226));
        cuc.getConversionItems().add(item);
      }
    }
  }
  
  @Test
  public void poundsToPoundsConversionNotNull() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "1";
    String toUnit = "1";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1d), result);
  }
  
  @Test
  public void poundsToPoundsConversionNull() {
    CropUnitConversion cuc = null;
    Double fromValue = 1d;
    String fromUnit = "1";
    String toUnit = "1";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1d), result);
  }
  
  @Test
  public void tonnesToPounds() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "2";
    String toUnit = "1";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(2204.6226), result);
  }
  
  @Test
  public void poundsToTonnes() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 2204.6226;
    String fromUnit = "1";
    String toUnit = "2";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1), result);
  }
  
  @Test
  public void poundsToLargeBales() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 2204.6226;
    String fromUnit = "1";
    String toUnit = "8";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(2.2046226), result);
  }
  
  @Test
  public void poundsToSmallBales() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 2204.6226;
    String fromUnit = "1";
    String toUnit = "7";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(36.74371), result);
  }
  
  @Test
  public void poundsToUnknown() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "1";
    String toUnit = "0";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNull(result);
  }
  
  @Test
  public void unknownToPounds() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "0";
    String toUnit = "1";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNull(result);
  }
  
  @Test
  public void unknownToTonnes() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "0";
    String toUnit = "2";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNull(result);
  }
  
  @Test
  public void tonnesToUnknown() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "2";
    String toUnit = "0";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNull(result);
  }
  
  @Test
  public void unknownToUnknown() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "0";
    String toUnit = "0";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1d), result);
  }
  
  @Test
  public void tonnesToKilograms() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "2";
    String toUnit = "5";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1000.0d), result);
  }
  
  @Test
  public void tonnesToGrams() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 1d;
    String fromUnit = "2";
    String toUnit = "6";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1000000d), result);
  }
  
  @Test
  public void poundsToKilograms() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 2204.6226;
    String fromUnit = "1";
    String toUnit = "5";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1000d), result);
  }
  
  @Test
  public void poundsToCwt() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 100d;
    String fromUnit = "1";
    String toUnit = "16";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1d), result);
  }
  
  @Test
  public void cwtToKilograms() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 22.046226;
    String fromUnit = "16";
    String toUnit = "5";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1000d), result);
  }
  
  @Test
  public void tonnesToPoundsWithNullConversion() {
    CropUnitConversion cuc = null;
    Double fromValue = 1d;
    String fromUnit = "2";
    String toUnit = "1";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(2204.6226), result);
  }
  
  @Test
  public void poundsToTonnesWithNullConversion() {
    CropUnitConversion cuc = null;
    Double fromValue = 2204.6226;
    String fromUnit = "1";
    String toUnit = "2";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1), result);
  }
  
  @Test
  public void poundsToKilogramsWithNullConversion() {
    CropUnitConversion cuc = conversions.get("5560");
    Double fromValue = 2204.6226;
    String fromUnit = "1";
    String toUnit = "5";
    
    Double result = CropUnitUtils.convert(cuc, fromValue, fromUnit, toUnit);
    assertNotNull(result);
    assertEquals(Double.valueOf(1000d), result);
  }
}
