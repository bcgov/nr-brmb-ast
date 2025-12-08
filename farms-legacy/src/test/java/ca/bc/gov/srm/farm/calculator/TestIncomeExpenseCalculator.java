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

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ClientService;
import  ca.bc.gov.srm.farm.service.impl.ClientServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 */
public class TestIncomeExpenseCalculator {

  private static Connection conn;

  @BeforeAll
  protected static void setUp() throws Exception {
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    TestUtils.closeConnection(conn);
  }
  

  @Test
  public void testKnownVerified() throws Exception {
  	// no yardage, payments, or contract work 
  	calcKnownFarmIncomeExpense(3231917, 2009);

  	// verified with contract work expenses
  	calcKnownFarmIncomeExpense(3755360, 2009);
  }

  private void calcKnownFarmIncomeExpense(int pin, int year) throws Exception {
    ClientService cs = ClientServiceFactory.getInstance(conn);
    Scenario sc = cs.getClientInfoWithHistory(pin, year, null, ClientService.DEF_FIRST_MODE);

    assertNotNull(sc);
    assertEquals(1, sc.getFarmingYear().getFarmingOperationCount());
    
    FarmingYear fy = sc.getFarmingYear();
    Integer farmNumber = new Integer(1);
    FarmingOperation fo = fy.getFarmingOperationByNumber(farmNumber);
    
    // income
    double totalIncome = fy.getMarginTotal().getTotalAllowableIncome().doubleValue();
    double farmIncome = fo.getMargin().getTotalAllowableIncome().doubleValue();
    assertEquals(totalIncome, farmIncome, 0.1);
    
    // expense
    double totalExpense = fy.getMarginTotal().getTotalAllowableExpenses().doubleValue();
    double farmExpense = fo.getMargin().getTotalAllowableExpenses().doubleValue();
    assertEquals(totalExpense, farmExpense, 0.1);
    
    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(sc);
    nullFixer.fixNulls(sc);

    // Need to calculate accrualAdjsPurchasedInputs for IncomeExpenseCalculator
    AccrualCalculator accrualCalc = CalculatorFactory.getAccrualCalculator(sc);
    accrualCalc.calculateTotals();

    // note that the calculator overwrite the values in the Margin object
    IncomeExpenseCalculator calc = CalculatorFactory.getIncomeExpenseCalculator(sc);
    calc.calculateIncomeExpense();
    
    double calculatedFarmIncome = fo.getMargin().getTotalAllowableIncome().doubleValue();
    assertEquals(farmIncome, calculatedFarmIncome, 0.1);
    
    double calculatedFarmExpense = fo.getMargin().getTotalAllowableExpenses().doubleValue();
    assertEquals(farmExpense, calculatedFarmExpense, 0.1);
    
    double calculatedTotalIncome = fy.getMarginTotal().getTotalAllowableIncome().doubleValue();
    assertEquals(totalIncome, calculatedTotalIncome, 0.1);
    
    double calculatedTotalExpense = fy.getMarginTotal().getTotalAllowableExpenses().doubleValue();
    assertEquals(totalExpense, calculatedTotalExpense, 0.1);
  }

}
