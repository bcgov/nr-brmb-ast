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
package ca.bc.gov.srm.farm.calculator;

import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 * @created Mar 4, 2011
 */
public class FmvCalculator {

  /**
   * 
   * @param item item
   * @return true if price exceeds varaiance
   */
  public boolean isPriceStartOutOfVariance(ProducedItem item) {
    Double fmvPrice = getFmvForPriceStart(item);
    return isPriceOutOfVariance(item.getTotalPriceStart(), fmvPrice, item.getFmvVariance());
  }
  
  /**
   * 
   * @param item item
   * @return true if price exceeds varaiance
   */
  public boolean isPriceEndOutOfVariance(ProducedItem item) {
    return isPriceOutOfVariance(item.getTotalPriceEnd(), item.getFmvEnd(), item.getFmvVariance());
  }
  
  /**
   * 
   * @param item item
   * @return true if price exceeds varaiance
   */
  public boolean isReportedPriceStartOutOfVariance(ProducedItem item) {
    Double fmvPrice = getFmvForPriceStart(item);
    return isPriceOutOfVariance(item.getReportedPriceStart(), fmvPrice, item.getFmvVariance());
  }
  
  /**
   * 
   * @param item item
   * @return true if price exceeds varaiance
   */
  public boolean isReportedPriceEndOutOfVariance(ProducedItem item) {
    return isPriceOutOfVariance(item.getReportedPriceEnd(), item.getFmvEnd(), item.getFmvVariance());
  }
  
  
  /**
	 * @param item item
	 * @return start price
	 */
  public Double calculateStartPrice(ProducedItem item) {
  	Double fmvPrice = getFmvForPriceStart(item);
    return calculatePrice(item.getTotalPriceStart(), fmvPrice, item.getFmvVariance());
  }

  /**
   * @param item item
   * @return the FMV for the previous year end if it is not null, otherwise the current year FMV start
   */
  private Double getFmvForPriceStart(ProducedItem item) {
    Double fmvPrice = item.getFmvPreviousYearEnd();
  	if(fmvPrice == null) {
  	  fmvPrice = item.getFmvStart();
  	}
    return fmvPrice;
  }
  
  /**
	 * @param item item
	 * @return end price
	 */
  public Double calculateEndPrice(ProducedItem item) {
  	Double priceEnd = item.getTotalPriceEnd();
    if(priceEnd == null && item.getFmvEnd() != null && item.getFmvVariance() != null) {
      if(item.getTotalEndYearProducerPrice() != null
          && item.getTotalEndYearProducerPrice().doubleValue() != 0) {
        priceEnd = item.getTotalEndYearProducerPrice();
      }
    }
    return calculatePrice(priceEnd, item.getFmvEnd(), item.getFmvVariance());
  }
  
  
  /**
   * 
   * @param price price
   * @param fmvPrice fmvPrice
   * @param variance variance
   * @return true if price exceeds varaiance
   */
  private boolean isPriceOutOfVariance(Double price, Double fmvPrice, Double variance) {
    boolean result;

    if(price == null || fmvPrice == null || variance == null) {
      result = false;
    } else {
      final int scale = 2;
      
      double value = 0;
      // round because doubles sometimes are a tiny fraction off the exact amount
      value = MathUtils.round(price.doubleValue(), scale);
      
      final int hundred = 100;
      double percent = variance.doubleValue() / hundred;
      // round because doubles sometimes are a tiny fraction off the exact amount
      double fmv = fmvPrice.doubleValue();
      
      double min = MathUtils.round(fmv - (fmv * percent), scale);
      double max = MathUtils.round(fmv + (fmv * percent), scale);
      
      if(value < min || value > max) {
        result = true;
      } else {
        result = false;
      }
    }
    
    return result;
  }
  
  
  
  /**
	 * See flow chart in "inventory valuation DJS 28Feb11.vsd" 
	 * 
	 * @param price price
	 * @param fmvPrice fmvPrice
	 * @param variance variance
	 * @return start price
	 */
  private Double calculatePrice(Double price, Double fmvPrice, Double variance) {
  	Double resultingPrice = null;
  	
  	if(price != null && !isPriceOutOfVariance(price, fmvPrice, variance)) {
  		//
  		// price OK so use it
  		//
  		resultingPrice = price;
  	} else if (price == null && fmvPrice != null) {
  		//
  		// no price given so use FMV
  		//
  		resultingPrice = fmvPrice;
  	} else if(fmvPrice != null && variance != null) {
  		//
  		// price given but exceeds price range use min or max 
  		//
      double value = 0;
      if(price != null) {
        value = price.doubleValue();
      }
      
      final int hundred = 100;
      double percent = variance.doubleValue() / hundred;
      double fmv = fmvPrice.doubleValue();
      
      double min = fmv - (fmv * percent);
      double max = fmv + (fmv * percent);
      
      if(value > max) {
      	resultingPrice = new Double(max);
      } else if (value < min) {
      	resultingPrice = new Double(min);
      } else {
      	resultingPrice = new Double(value);
      }
  	}
    
    final int decimalPlaces = 2;
    resultingPrice = MathUtils.round(resultingPrice, decimalPlaces);
  	
  	return resultingPrice;
  }
}
