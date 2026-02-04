package ca.bc.gov.srm.farm.ui.struts.fifo;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.fifo.FifoResults;

public class FifoResultForm extends ValidatorForm {

  private static final long serialVersionUID = 7151331648313929589L;

  private Integer importVersionId;

  private FifoResults fifoResults;
  
  public Integer getImportVersionId() {
    return importVersionId;
  }

  public FifoResults getFifoResults() {
    return fifoResults;
  }

  public void setImportVersionId(Integer importVersionId) {
    this.importVersionId = importVersionId;
  }

  public void setFifoResults(FifoResults fifoResults) {
    this.fifoResults = fifoResults;
  }

  public int getNumFifoItemResults() {
    if (fifoResults.getFifoItemResults() == null) {
      return 0;
    }
    return fifoResults.getFifoItemResults().size();
  }

}
