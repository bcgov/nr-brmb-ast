package ca.bc.gov.srm.farm.reasonability;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.ExpenseTestRefYearCompGCResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * Expense Test - Reference Year Comparison
 * 
 * @author awilkinson
 */
public class ExpenseTestRefYearCompGC extends ReasonabilityTest {
  private static Logger logger = LoggerFactory.getLogger(ExpenseTestIAC.class);

  public static final String ERROR_MISSING_REFERENCE_YEAR_EXPENSES =
      "Must have expenses for the three most recent reference years.";
  
  private double industryVarianceLimit;

  public ExpenseTestRefYearCompGC() {    
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    
    industryVarianceLimit = config.getExpenseGCVariance();
  }
  
  @Override
  public void test(Scenario scenario) throws ReasonabilityTestException {
    LoggingUtils.logMethodStart(logger);
    
    ExpenseTestRefYearCompGCResult result = new ExpenseTestRefYearCompGCResult();
    result.setVarianceLimit(industryVarianceLimit);
    scenario.getReasonabilityTestResults().setExpenseTestRefYearCompGC(result);
    List<ReasonabilityTestResultMessage> errors = new ArrayList<>();
    
    if (missingRequiredData(scenario)) {
      throw new ReasonabilityTestException(MessageConstants.ERRORS_REASONABILITY_BENEFIT_NOT_CALCULATED);
    }
    
    final int programYear = scenario.getYear();
    final int n1 = 1;
    final int n2 = 2;
    final int n3 = 3;
    final int n4 = 4;
    final int n5 = 5;
    
    int numOfRefYears = 0;
    double totalRefYrsExpense = 0;
    
    for (ReferenceScenario refScen : scenario.getReferenceScenarios()) {
      int refYear = refScen.getYear();
      double refYearStrucExpense = refScen.getFarmingYear().getMarginTotal().getExpensesAfterStructuralChange();
      totalRefYrsExpense += refYearStrucExpense;
      
      if(refYearStrucExpense != 0) {
        numOfRefYears++;
      }
      
      if(refYear == (programYear - n1)) {
        result.setExpenseStructuralChangeYearMinus1(refYearStrucExpense);
      } else if(refYear == (programYear - n2)) {
        result.setExpenseStructuralChangeYearMinus2(refYearStrucExpense);
      } else if(refYear == (programYear - n3)) {
        result.setExpenseStructuralChangeYearMinus3(refYearStrucExpense);
      } else if(refYear == (programYear - n4)) {
        result.setExpenseStructuralChangeYearMinus4(refYearStrucExpense);
      } else if(refYear == (programYear - n5)) {
        result.setExpenseStructuralChangeYearMinus5(refYearStrucExpense);
      }      
    }
    
    boolean hasThreeMostRecentReferenceYears =
        result.getExpenseStructuralChangeYearMinus1() != null && result.getExpenseStructuralChangeYearMinus1() != 0
        && result.getExpenseStructuralChangeYearMinus2() != null && result.getExpenseStructuralChangeYearMinus2() != 0
        && result.getExpenseStructuralChangeYearMinus3() != null && result.getExpenseStructuralChangeYearMinus3() != 0;
    
    Double crntYrExpAccruAdj = scenario.getFarmingYear().getMarginTotal().getExpenseAccrualAdjs();
    result.setProgramYearAcrAdjExpense(crntYrExpAccruAdj);
    
    if (hasThreeMostRecentReferenceYears) {
      Double averageRefYrsExpense = totalRefYrsExpense/numOfRefYears;
      result.setExpenseStructrualChangeAverage(averageRefYrsExpense);
      
      Double variance = null;
      if (averageRefYrsExpense == 0) {
        result.setResult(Boolean.FALSE);
      } else {
        variance = ReasonabilityTestUtils.roundPercent((crntYrExpAccruAdj / averageRefYrsExpense) - 1);
        if (Math.abs(variance) > industryVarianceLimit) {
          result.setResult(Boolean.FALSE);
        } else {
          result.setResult(Boolean.TRUE);
        }
      }
      result.setVariance(variance);
    } else {
      result.setResult(false);
      ReasonabilityTestUtils.addErrorMessage(errors, ERROR_MISSING_REFERENCE_YEAR_EXPENSES);
    }
    
    result.getErrorMessages().addAll(errors);
    
    LoggingUtils.logMethodStart(logger);
  }

  public double getIndustryVarianceLimit() {
    return industryVarianceLimit;
  }

  public static boolean missingRequiredData(Scenario scenario) {
    boolean hasRequiredData = true;
    for (ReferenceScenario refScen : scenario.getReferenceScenarios()) {
      if(refScen.getFarmingYear() == null
          ||refScen.getFarmingYear().getMarginTotal() == null
          || refScen.getFarmingYear().getMarginTotal().getExpensesAfterStructuralChange() == null) {
        hasRequiredData = false;
        break;
      }
    }
    
    hasRequiredData = hasRequiredData
        && scenario.getFarmingYear() != null
        && scenario.getFarmingYear().getMarginTotal() != null
        && scenario.getFarmingYear().getMarginTotal().getExpenseAccrualAdjs() != null;

    return !hasRequiredData;
  }
}
