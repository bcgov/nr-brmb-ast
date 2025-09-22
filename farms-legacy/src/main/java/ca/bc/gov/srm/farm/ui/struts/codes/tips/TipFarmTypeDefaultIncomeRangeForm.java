package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;

public class TipFarmTypeDefaultIncomeRangeForm extends ValidatorForm {
  private static final long serialVersionUID = 1437540855736390986L;
  
  private List<TipFarmTypeIncomeRange> incomeRange;
  private String incomeRangeJson;
  
  public List<TipFarmTypeIncomeRange> getIncomeRange() {
    return incomeRange;
  }
  
  public void setIncomeRange(List<TipFarmTypeIncomeRange> incomeRange) {
    this.incomeRange = incomeRange;
  }
  
  public String getIncomeRangeJson() {
    return incomeRangeJson;
  }
  
  public void setIncomeRangeJson(String incomeRangeJson) {
    this.incomeRangeJson = incomeRangeJson;
  }
}
