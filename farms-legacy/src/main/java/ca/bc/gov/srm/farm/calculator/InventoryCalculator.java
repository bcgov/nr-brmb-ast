package ca.bc.gov.srm.farm.calculator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.StringUtils;


/**
 * See flow chart in "inventory valuation DJS 28Feb11.vsd" 
 */
public class InventoryCalculator {

  private Logger logger = LoggerFactory.getLogger(InventoryCalculator.class);
  
  private FmvCalculator fmvCalculator = CalculatorFactory.getFmvCalculator();
	
	/**
	 * 
	 * @param inventoryItem inventoryItem
	 * @return true if crop or livestock
	 */
	public boolean isInventory(InventoryItem inventoryItem) {
		String classCode = inventoryItem.getInventoryClassCode();
		boolean isCrop = InventoryClassCodes.CROP.equals(classCode);
		boolean isLivestock = InventoryClassCodes.LIVESTOCK.equals(classCode);
		return (isCrop || isLivestock);
	}
	
	
	/**
	 * 
	 * @param inventoryItem inventoryItem
	 * @return true if payable
	 */
	public boolean isPayable(InventoryItem inventoryItem) {
		String classCode = inventoryItem.getInventoryClassCode();
		boolean isPayable = InventoryClassCodes.PAYABLE.equals(classCode);
		return isPayable;
	}
	
	
	/**
	 * @param producedItem producedItem
	 * @param quantityStart quantityStart
	 * @param endPrice endPrice
	 * @return start price
	 */
	public Double getStartPrice(ProducedItem producedItem, Double quantityStart, Double endPrice) {
    Double startPrice = null;
    
    // see if this a P2 item (P2 is non-market commodity)
    boolean isMarketCommodity = isMarketCommodity(producedItem);
    
    if(!isMarketCommodity) {

      if(endPrice != null) {
        startPrice = endPrice;
      } else {
        startPrice = producedItem.getReportedPriceStart();
      }

    } else if(quantityStart == null) {

	    startPrice = producedItem.getReportedPriceStart();

	  } else {

  		boolean useFmvValuation = false;
  		
  		if(producedItem.getTotalPriceStart() == null) {
  			// Start price wasn't given

        List<InventoryItem> sameYearMatchingInventoryItems = getSameYearMatchingInventoryItems(producedItem);
        boolean hasDuplicates = sameYearMatchingInventoryItems.size() > 1;
        if(!hasDuplicates) {

  				//
  				// try to use last year's end price
  				//
  				InventoryItem prevYearInv = getPreviousYearInventoryItem(producedItem);
  				
  				if(prevYearInv != null && prevYearInv.getTotalPriceEnd() != null) {
  					ProducedItem prevPi = (ProducedItem) prevYearInv;
  				  
  				  if(fmvCalculator.isPriceEndOutOfVariance(prevPi)) {
  				  	useFmvValuation = true;
  				  } else {
  				  	// prev year's end price exists and is within FMV tolerance
  				  	startPrice = prevYearInv.getTotalPriceEnd();
  				  }
  				} else {
  					useFmvValuation = true;
  				}
        }
  		} else {
  			//
  			// Start price was given. See if it is within the FMV variance
  			//
  			if(fmvCalculator.isPriceStartOutOfVariance(producedItem)) {
  				useFmvValuation = true;
  			} else {
  				// price exists and is within FMV tolerance, so use it
  				startPrice = producedItem.getTotalPriceStart();
  			}
  		}
  		
  		if(useFmvValuation) {
  			startPrice = fmvCalculator.calculateStartPrice(producedItem);
  		}
	  }
		
		return startPrice;
	}


	/**
	 * @param producedItem inventoryItem
	 * @return boolean
	 */
  public boolean isMarketCommodity(ProducedItem producedItem) {
    boolean isMarketCommodity = true;
    if(producedItem instanceof LivestockItem) {
      LivestockItem li = (LivestockItem) producedItem;
      
      if(li.getIsMarketCommodity() != null) {
        isMarketCommodity = li.getIsMarketCommodity().booleanValue();
      }
    }
    return isMarketCommodity;
  }
	
	
	/**
	 * @param producedItem producedItem
	 * @param quantityEnd quantityEnd
	 * @return end price
	 */
	public Double getEndPrice(ProducedItem producedItem, Double quantityEnd) {
    
    Double endPrice;
    if(quantityEnd == null) {
      endPrice = producedItem.getReportedPriceEnd();
    } else {
      endPrice = fmvCalculator.calculateEndPrice(producedItem);
    }
		
		return endPrice;
	}
		
	
	/**
	 * 
	 * @param producedItem producedItem
	 * @return start quantity
	 */
	public Double getStartQuantity(ProducedItem producedItem) {
		Double startQuantity = null;
		
		if(producedItem.getTotalQuantityStart() == null) {
      List<InventoryItem> sameYearMatchingInventoryItems = getSameYearMatchingInventoryItems(producedItem);
      boolean hasDuplicates = sameYearMatchingInventoryItems.size() > 1;
      if(!hasDuplicates) {
  			//
  			// Use the previous year's end quantity. Oddly enough, we DO NOT
  			// want to use the "getEndQuantity" logic in this case. We are
  			// just supposed to try to get it, and if is null, then we
  			// just set it to 0.
  			//
  			InventoryItem prevYearInv = getPreviousYearInventoryItem(producedItem);
  			
  			if(prevYearInv != null) {
  				Double prevEndQuantity = prevYearInv.getTotalQuantityEnd();
  				
  				if(prevEndQuantity == null) {
  					logger.debug("Previous year end quantity is null");
  				} else {
  					startQuantity = prevEndQuantity;
  				}
  			}
      }
		} else {
			startQuantity = producedItem.getTotalQuantityStart();
		}
		
		return startQuantity;
	}
	
	
	/**
	 * 
	 * @param inventoryItem inventoryItem
	 * @return end quantity
	 */
	public Double getEndQuantity(ProducedItem inventoryItem) {
		Double endQuantity = inventoryItem.getTotalQuantityEnd();
		
		return endQuantity;
	}
  
  
  /**
   * @param inventoryItem inventoryItem
   * @return start of year amount
   */
  public Double getStartOfYearAmount(InventoryItem inventoryItem) {
    Double startAmount = null;

    if(inventoryItem.getTotalStartOfYearAmount() == null) {
      // Start of year amount wasn't given

      InventoryItem prevYearInv = getPreviousYearInventoryItem(inventoryItem);
      
      if(prevYearInv != null && prevYearInv.getTotalEndOfYearAmount() != null) {
        // prev year's end of year amount exists
        startAmount = prevYearInv.getTotalEndOfYearAmount();
      }
    } else {
      // Start of year amount was given so use it
      startAmount = inventoryItem.getTotalStartOfYearAmount();
    }
    
    return startAmount;
  }
  
  
  /**
   * @param inventoryItem inventoryItem
   * @return start of year amount
   */
  public Double getEndOfYearAmount(InventoryItem inventoryItem) {
    Double endAmount = inventoryItem.getTotalEndOfYearAmount();
    
    return endAmount;
  }
	
	
	/**
	 * @param invItem invItem
	 * @return InventoryItem
	 */
	public InventoryItem getPreviousYearInventoryItem(InventoryItem invItem) {
    return getAdjacentYearInventoryItem(invItem, -1);
  }
	
	/**
	 * @param invItem invItem
	 * @return InventoryItem
	 */
  public InventoryItem getNextYearInventoryItem(InventoryItem invItem) {
	  return getAdjacentYearInventoryItem(invItem, 1);
	}

	/**
	 * @param invItem invItem
   * @param releativeYearPosition number of years ahead or behind (negative for behind, postive for ahead)
	 * @return InventoryItem
	 */
	private InventoryItem getAdjacentYearInventoryItem(InventoryItem invItem, int releativeYearPosition) {
	  InventoryItem adjacentYearInv;
	  List<InventoryItem> adjacentYearItems = getAdjacentYearInventoryItems(invItem, releativeYearPosition);
	  // for consistency, we can only return a matching item if there is exactly one match.
	  if(adjacentYearItems == null || adjacentYearItems.size() != 1) {
	    adjacentYearInv = null;
	  } else {
	    adjacentYearInv = adjacentYearItems.get(0);
	  }
	  return adjacentYearInv;
	}


	/**
	 * @param invItem invItem
	 * @return List<InventoryItem>
	 */
	public List<InventoryItem> getNextYearInventoryItems(InventoryItem invItem) {
	  return getAdjacentYearInventoryItems(invItem, 1);
	}
	
	/**
	 * @param invItem invItem
	 * @return List<InventoryItem>
	 */
	public List<InventoryItem> getSameYearMatchingInventoryItems(InventoryItem invItem) {
	  return getAdjacentYearInventoryItems(invItem, 0);
	}


	/**
   * @param invItem invItem
   * @param releativeYearPosition number of years ahead or behind (negative for behind, postive for ahead)
   * @return List<InventoryItem>
   */
  private List<InventoryItem> getAdjacentYearInventoryItems(InventoryItem invItem, int releativeYearPosition) {
		List<InventoryItem> adjacentYearItems = null;
    if(invItem.getFarmingOperation() != null) {
      
      FarmingOperation prevFo = getAdjacentYearFarmingOperation(invItem, releativeYearPosition);
      	
    	if(prevFo != null) {
    	  List<InventoryItem> prevFoItems = prevFo.getInventoryItems();
        adjacentYearItems = findMatchingItems(invItem, prevFoItems);
    	}
      
    }
    
    if(adjacentYearItems == null) {
      adjacentYearItems = new ArrayList<>();
    }
		
		return adjacentYearItems;
	}


  /**
   * @param invItem invItem
   * @param items items
   * @return List<InventoryItem>
   */
  public List<InventoryItem> findMatchingItems(InventoryItem invItem, List<InventoryItem> items) {
    List<InventoryItem> matchingItems = new ArrayList<>();

    String cropUnit1 = null;
    if(invItem instanceof CropItem) {
      CropItem cropItem1 = (CropItem) invItem;
      cropUnit1 = cropItem1.getCropUnitCode();
    }

    for(InventoryItem ii : items) {
    	
    	if(ii.getInventoryItemCode().equals(invItem.getInventoryItemCode())
    	    && ii.getInventoryClassCode().equals(invItem.getInventoryClassCode())) {
        String cropUnit2 = null;
        if(ii instanceof CropItem) {
          CropItem cropItem2 = (CropItem) ii;
          cropUnit2 = cropItem2.getCropUnitCode();
        }
        
        if(StringUtils.equal(cropUnit1, cropUnit2)) {
          matchingItems.add(ii);
        }
    	}
    }
    return matchingItems;
  }
  
  
  /**
   * @param invItem invItem
   * @return FarmingOperation
   */
  public FarmingOperation getNextYearFarmingOperation(InventoryItem invItem) {
    return getAdjacentYearFarmingOperation(invItem, 1);
  }

  /**
   * @param curYearItem curYearItem
   * @param releativeYearPosition number of years ahead or behind (negative for behind, postive for ahead)
   * @return FarmingOperation
   */
  private static FarmingOperation getAdjacentYearFarmingOperation(
      InventoryItem curYearItem,
      int releativeYearPosition) {
    FarmingOperation nextYearOperation = null;
    FarmingOperation curYearOperation = curYearItem.getFarmingOperation();
    ReferenceScenario curYearRefScenario = curYearOperation.getFarmingYear().getReferenceScenario();
    Scenario scenario = curYearRefScenario.getParentScenario();
    String schedule = curYearOperation.getSchedule();
    int curYear = curYearRefScenario.getYear().intValue();
    int yearToLookFor = curYear + releativeYearPosition;
    ReferenceScenario nextYearRefScenario = scenario.getReferenceScenarioByYear(new Integer(yearToLookFor));

    if(nextYearRefScenario != null) {
      nextYearOperation =
          nextYearRefScenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
    }
    
    return nextYearOperation;
  }
  
  
  /**
   * 
   * @param inventoryItem inventoryItem
   * @return changInValue
   */
  public double calculateChangeInValue(InventoryItem inventoryItem) {
    double startValue;
    double endValue;
    double changInValue;
    
    if(isInventory(inventoryItem)) {
      //
      // For crop and livestock use the start and end prices and quantities
      //
      double quantityStart = MathUtils.getPrimitiveValue(inventoryItem.getTotalQuantityStart());
      double priceStart = MathUtils.getPrimitiveValue(inventoryItem.getTotalPriceStart());
      double quantityEnd = MathUtils.getPrimitiveValue(inventoryItem.getTotalQuantityEnd());
      double priceEnd = MathUtils.getPrimitiveValue(inventoryItem.getTotalPriceEnd());

      startValue = quantityStart * priceStart;
      
      endValue = quantityEnd * priceEnd;
      changInValue = endValue - startValue;
    } else {
      //
      // For other types, just use what is in the START_OF_YEAR_AMOUNT 
      // and END_OF_YEAR_AMOUNT columns. Note that nulls are treated as 0.
      //
      startValue = MathUtils.getPrimitiveValue(inventoryItem.getTotalStartOfYearAmount());
      endValue = MathUtils.getPrimitiveValue(inventoryItem.getTotalEndOfYearAmount());
      
      if(isPayable(inventoryItem)) {
        // The change in value for payables is the start value minus the end value
        changInValue = startValue - endValue;
      } else {
        changInValue = endValue - startValue;
      }
    }
    
    return changInValue;
  }
}
