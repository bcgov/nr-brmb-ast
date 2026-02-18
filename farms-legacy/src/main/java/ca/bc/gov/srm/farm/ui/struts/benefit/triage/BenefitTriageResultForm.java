package ca.bc.gov.srm.farm.ui.struts.benefit.triage;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageResults;

public class BenefitTriageResultForm extends ValidatorForm {

  private static final long serialVersionUID = 7151331648313929589L;

  private Integer importVersionId;

  private BenefitTriageResults triageResults;
  
  public Integer getImportVersionId() {
    return importVersionId;
  }

  public BenefitTriageResults getTriageResults() {
    return triageResults;
  }

  public void setImportVersionId(Integer importVersionId) {
    this.importVersionId = importVersionId;
  }

  public void setTriageResults(BenefitTriageResults triageResults) {
    this.triageResults = triageResults;
  }

  public int getNumTriageItemResults() {
    if (triageResults.getTriageItemResults() == null) {
      return 0;
    }
    return triageResults.getTriageItemResults().size();
  }

}
