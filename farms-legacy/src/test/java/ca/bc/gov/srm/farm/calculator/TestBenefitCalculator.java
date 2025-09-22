/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.dao.TestModelBuilder2019;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class TestBenefitCalculator {
  
  @Test
  public void testBenefitCalculation2018() throws Exception {
    
    int programYear = 2018;
    Scenario scenario = TestModelBuilder2019.getScenario(programYear);

    { // program year
      FarmingYear farmingYear = scenario.getFarmingYear();

      List<FarmingOperation> farmingOperations = new ArrayList<>();
      {
        FarmingOperation fo = TestModelBuilder2019.getFarmingOperation(programYear, "A", 1);
        farmingOperations.add(fo);
        farmingYear.setFarmingOperations(farmingOperations);
        
//        List<CropItem> cropItems = new ArrayList<>();
//        {
//          CropItem cropItem = new CropItem();
//          cropItems.add(cropItem);
//        }
//        fo.setCropItems(cropItems);
        
        { // start Income/Expenses
          List<IncomeExpense> incomeExpenses = new ArrayList<>();
          
          // Eligible Income
          {
            {
              IncomeExpense income = new IncomeExpense();
              LineItem lineItem = TestModelBuilder2019.getLineItem426(programYear);
              income.setLineItem(lineItem);
              income.setReportedAmount(12240.00);
              income.setIsExpense(false);
              
              incomeExpenses.add(income);
            }
          }
          
          // Eligible Expenses
          {
            {
              IncomeExpense expense = new IncomeExpense();
              LineItem lineItem = TestModelBuilder2019.getLineItem67(programYear);
              expense.setLineItem(lineItem);
              expense.setReportedAmount(25000.00);
              expense.setIsExpense(true);
              
              incomeExpenses.add(expense);
            }
          }
          fo.setIncomeExpenses(incomeExpenses);
        } // end Income/Expenses
        
        { // start Productive Units
          List<ProductiveUnitCapacity> pucs = new ArrayList<>();
          
          {
            {
              
              ProductiveUnitCapacity puc = TestModelBuilder2019.getPucWithInventoryCode("5060", 18.999);
              pucs.add(puc);
            }
          }
          
          fo.setProductiveUnitCapacities(pucs);
        } // end Productive Units
      }
      
    }
  }

}
