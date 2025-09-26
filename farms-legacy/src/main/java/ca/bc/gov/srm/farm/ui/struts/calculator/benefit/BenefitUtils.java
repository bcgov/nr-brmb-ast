package ca.bc.gov.srm.farm.ui.struts.calculator.benefit;

import java.util.List;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.ScenarioUtils;


/**
 * Stuff that is needed by more one than one action, and is
 * too ugly to put into the form.
 */
public final class BenefitUtils {

  /**
   * private constructor
   */
  private BenefitUtils() {
  }
	
	/**
	 * 
	 * @param scenario scenario
	 * @param farmView farmView
	 * @return Object[]
	 */
	public static Object[] getTotalsForFarmView(Scenario scenario, String farmView) {
	  List<Integer> allYears = ScenarioUtils.getAllYears(scenario.getYear());
		Object[] totals = new Object[allYears.size()];
	  int ii = 0;
	  boolean wholeFarm = CalculatorAction.FARM_VIEW_WHOLE_FARM_CODE.equals(farmView);
	  
	  if(wholeFarm) {
	  	
			for(Integer curYear : allYears) {
        ReferenceScenario rs = scenario.getReferenceScenarioByYear(curYear);
				
        if(rs != null) {
          totals[ii] = rs.getFarmingYear().getMarginTotal();
        }
        ii++;
			}
			
	  } else {
	  	String schedule = farmView;
  		FarmingOperation fo = null;
  		
			for(ReferenceScenario rs : scenario.getAllScenarios()) {
				fo = rs.getFarmingYear().getFarmingOperationBySchedule(schedule);

				if(fo != null) {
					totals[ii] = fo.getMargin();
				}
				ii++;
			}
	  }
	  
	  return totals;
	}

  public static Double getAgristabilityTotalBenefit(Scenario scenario) {
    Benefit benefit = scenario.getFarmingYear().getBenefit();
    int programYear = scenario.getYear().intValue();
    return getAgristabilityTotalBenefit(programYear, benefit);
  }

  public static Double getAgristabilityTotalBenefit(int programYear, Benefit benefit) {
    Double benefitAmount;
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
      benefitAmount = benefit.getStandardBenefit();
    } else {
      benefitAmount = benefit.getTotalBenefit();
    }
    return benefitAmount;
  }
}
