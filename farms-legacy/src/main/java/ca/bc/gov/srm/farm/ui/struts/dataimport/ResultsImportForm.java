package ca.bc.gov.srm.farm.ui.struts.dataimport;

import org.apache.struts.validator.ValidatorForm;


/**
 * ImportStatusForm used by screen 230.
 */
public class ResultsImportForm extends ValidatorForm {

    private static final long serialVersionUID = -1645506621365403099L;
    
    private String importVersionId;
    private String tab;
    
    private boolean hasFileForDownload;
    
		public String getImportVersionId() {
			return importVersionId;
		}
		
		public void setImportVersionId(String importVersionId) {
			this.importVersionId = importVersionId;
		}

		public String getTab() {
			return tab;
		}

		public void setTab(String tab) {
			this.tab = tab;
		}

    public boolean isHasFileForDownload() {
      return hasFileForDownload;
    }

    public void setHasFileForDownload(boolean hasFileForDownload) {
      this.hasFileForDownload = hasFileForDownload;
    }
    
}
