package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TipFarmTypeIncomeRangeFormData {
  private List<TipFarmTypeIncomeRangeFormItem> incomeRange;

  public List<TipFarmTypeIncomeRangeFormItem> getIncomeRange() {
    return incomeRange;
  }

  public void setIncomeRange(List<TipFarmTypeIncomeRangeFormItem> incomeRange) {
    this.incomeRange = incomeRange;
  }
}
