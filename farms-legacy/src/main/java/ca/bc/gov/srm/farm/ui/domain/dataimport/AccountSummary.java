package ca.bc.gov.srm.farm.ui.domain.dataimport;

/**
 * Bean used by screen 230.
 */
public class AccountSummary {

  private String pin;

  private String name;

  private String status;

  private String errorMessage;


  /**
   * @return  the errorMessage
   */
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * @param  errorMessage  the errorMessage to set
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * @return  the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param  name  the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return  the pin
   */
  public String getPin() {
    return pin;
  }

  /**
   * @param  pin  the pin to set
   */
  public void setPin(String pin) {
    this.pin = pin;
  }

  /**
   * @return  the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * @param  status  the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

}
