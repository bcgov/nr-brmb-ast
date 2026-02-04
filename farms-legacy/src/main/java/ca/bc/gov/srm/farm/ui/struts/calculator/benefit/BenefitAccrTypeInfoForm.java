package ca.bc.gov.srm.farm.ui.struts.calculator.benefit;

import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * @author hmacphail
 * @created Oct 23, 2013
 */
public class BenefitAccrTypeInfoForm extends CalculatorForm {

  private static final long serialVersionUID = 4826477109823672189L;
  
  private String accrAdjType;

  public BenefitAccrTypeInfoForm() {
	  
  }
  
  /**
   * @return the accrAdjType
   */
  public String getAccrAdjType() {
    return accrAdjType;
  }

  /**
   * @param accrAdjType the accrAdjType to set the value to
   */
  public void setAccrAdjType(String accrAdjType) {
    this.accrAdjType = accrAdjType;
  }

}
