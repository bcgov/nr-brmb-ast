package ca.bc.gov.srm.farm.calculator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * Before doing any benefit calculations, make sure we have the
 * objects used to store the results.
 */
public abstract class BenefitNullFixer {

  /**
   * @param programYearScenario Scenario
   */
  public abstract void fixNulls(Scenario programYearScenario);
  
  /**
   * @param fy FarmingYear
   */
  protected void fixNulls(FarmingYear fy) {
    if(fy.getMarginTotal() == null) {
      fy.setMarginTotal(new MarginTotal());
    }
    
    if(fy.getFarmingOperations() == null) {
      fy.setFarmingOperations(new ArrayList<FarmingOperation>());
    }
    
    for(FarmingOperation fo : fy.getFarmingOperations()) {
      
      if(fo.getMargin() == null) {
        fo.setMargin(new Margin());
      }
    }
  }
  
  
  
  /**
   * calculate bpuLeadInd if it is null. The user can override this flag
   * in the GUI.
   * 
   * @param programYearScenario programYearScenario
   */
  protected void calculateBpuLeadInd(Scenario programYearScenario) {
    MarginTotal mt = programYearScenario.getFarmingYear().getMarginTotal();
    final int dayOfMonth = 31;
    Integer opNum = new Integer(1);
    
    //
    // Always set it to false for the program year, because the BPUs start
    // on the previous years. See ProductiveValueCalculator.getBasePriceForYear
    //
    mt.setBpuLeadInd(Boolean.FALSE);
    
    for(ReferenceScenario rs : programYearScenario.getReferenceScenarios()) {
      mt = rs.getFarmingYear().getMarginTotal();
      
      if(mt.getBpuLeadInd() == null) {
        FarmingYear fy = rs.getFarmingYear();
        FarmingOperation op = fy.getFarmingOperationByNumber(opNum);
        
        mt.setBpuLeadInd(Boolean.FALSE);
        
        if(op == null) {
          continue;
        }
        
        Date yearEnd = op.getFiscalYearEnd();
        if(yearEnd == null) {
          continue;
        }
        
        //
        // If the fiscal year end is after Jul 31 then set lead true
        //
        Calendar cal = Calendar.getInstance();
        cal.setTime(yearEnd);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        cal.set(Calendar.MONTH, Calendar.JULY);
        
        Date july31 = cal.getTime();
        
        if(yearEnd.after(july31)) {
          mt.setBpuLeadInd(Boolean.TRUE);
        }
      }
    }
  }
}
