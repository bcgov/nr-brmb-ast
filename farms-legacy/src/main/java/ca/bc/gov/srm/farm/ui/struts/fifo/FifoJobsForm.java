package ca.bc.gov.srm.farm.ui.struts.fifo;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.ui.domain.dataimport.ImportSearchResult;

public class FifoJobsForm extends ValidatorForm {

  private static final long serialVersionUID = 3976250657323431735L;
  
  private List<ImportSearchResult> searchResults;

  private boolean newImportAllowed;
  
  public boolean getNewImportAllowed() {
    return newImportAllowed;
  }

  public void setNewImportAllowed(boolean newImportAllowed) {
    this.newImportAllowed = newImportAllowed;
  }

  public List<ImportSearchResult> getSearchResults() {
    return searchResults;
  }

  public void setSearchResults(List<ImportSearchResult> searchResults) {
    this.searchResults = searchResults;
  }

  public int getNumSearchResults() {
    return searchResults.size();
  }

}
