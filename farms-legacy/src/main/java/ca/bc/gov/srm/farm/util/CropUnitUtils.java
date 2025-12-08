package ca.bc.gov.srm.farm.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.CropUnitConversionItem;

public final class CropUnitUtils {
  
  private static final MathContext MATH_CONTEXT = new MathContext(14, RoundingMode.HALF_UP);
  
  private static final Map<String, BigDecimal> STATIC_CONVERSIONS_FROM_TONNES = new HashMap<>();
  static {
    STATIC_CONVERSIONS_FROM_TONNES.put(CropUnitCodes.POUNDS,            BigDecimal.valueOf(2204.622600d));
    STATIC_CONVERSIONS_FROM_TONNES.put(CropUnitCodes.KILOGRAM,          BigDecimal.valueOf(1000.000000d));
    STATIC_CONVERSIONS_FROM_TONNES.put(CropUnitCodes.GRAM,              BigDecimal.valueOf(1000000.000000d));
    STATIC_CONVERSIONS_FROM_TONNES.put(CropUnitCodes.CWT_HUNDREDWEIGHT, BigDecimal.valueOf(22.046226));
  }
  
  private CropUnitUtils() {
    // private constructor
  }
  
  public static BigDecimal convert(CropUnitConversion cuc, BigDecimal value, String from, String to) {
    BigDecimal converted = null;
    
    // If from and to units are the same or the value is zero, then conversion is not needed.
    if(from.equals(to) || value.doubleValue() == 0) {
      converted = value;
    } else {
      
      List<CropUnitConversionItem> conversionItems;
      String defaultUnit;
      if (cuc == null) {
        conversionItems = null;
        defaultUnit = CropUnitCodes.TONNES;
      } else {
        defaultUnit = cuc.getDefaultCropUnitCode();
        conversionItems = cuc.getConversionItems();
      }

      BigDecimal valueInDefaultUnits = null;
      if(from.equals(defaultUnit)) {
        valueInDefaultUnits = value;
      } else {
        BigDecimal conversionFactorForFrom = getConversionFactor(defaultUnit, conversionItems, from);
        if(conversionFactorForFrom != null) {
          valueInDefaultUnits = value.divide(conversionFactorForFrom, MATH_CONTEXT);
        }
      }
      
      if(valueInDefaultUnits != null) {
        if(to.equals(defaultUnit)) {
          converted = valueInDefaultUnits;
        } else {
          BigDecimal conversionFactorForTo = getConversionFactor(defaultUnit, conversionItems, to);
          if(conversionFactorForTo != null) {
            converted = valueInDefaultUnits.multiply(conversionFactorForTo, MATH_CONTEXT);
          }
        }
      }
      
    }
    
    return converted;
  }
  
  /**
   * @return val converted from one unit to another, or return null if val is null.
   */
  public static Double convert(CropUnitConversion cu, Double val, String from, String to) {
    Double result = null;
    if(val != null) {
      BigDecimal converted = convert(cu, BigDecimal.valueOf(val), from, to);
      result = MathUtils.toDouble(converted);
    }
    return result;
  }

  public static BigDecimal convertPricePerUnit(CropUnitConversion cropUnitConversion, BigDecimal price, String from, String to) {
    // When converting price per unit, reverse the order of to/from to calculate the inverse.
    return convert(cropUnitConversion, price, to, from);
  }
  
  public static Double convertPricePerUnit(CropUnitConversion cropUnitConversion, Double price, String from, String to) {
    // When converting price per unit, reverse the order of to/from to calculate the inverse.
    return convert(cropUnitConversion, price, to, from);
  }
  
  private static BigDecimal getConversionFactor(String defaultUnit, List<CropUnitConversionItem> conversionItems, String conversionTo) {
    BigDecimal result = null;
    if(conversionItems != null) {
      for (CropUnitConversionItem item : conversionItems) {
        if (item.getTargetCropUnitCode().equals(conversionTo)) {
          result = item.getConversionFactor();
          break;
        }
      }
    }
    
    if(result == null && defaultUnit.contentEquals(CropUnitCodes.TONNES)) {
      result = STATIC_CONVERSIONS_FROM_TONNES.get(conversionTo);
    }
    return result;
  }
}