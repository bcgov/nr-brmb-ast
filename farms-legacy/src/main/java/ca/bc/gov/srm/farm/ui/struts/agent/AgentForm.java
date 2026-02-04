package ca.bc.gov.srm.farm.ui.struts.agent;

import org.apache.struts.validator.ValidatorForm;


/**
 * ReportForm.
 */
public class AgentForm extends ValidatorForm {

  private static final long serialVersionUID = -1845502921365403099L;

  private boolean isRunning;

  /**
   * @return  the isRunning
   */
  public boolean getIsRunning() {
    return isRunning;
  }

  /**
   * @param  isRunning  the isRunning to set
   */
  public void setIsRunning(boolean isRunning) {
    this.isRunning = isRunning;
  }
}
