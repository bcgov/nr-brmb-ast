package ca.bc.gov.srm.farm.ui.domain;

/**
 * Search results bean used by screen 320.
 */
public class AccountSearchResult {

  private String name;

  private String pin;

  private String address;


  /**
   * @return  the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param  address  the address to set
   */
  public void setAddress(String address) {
    this.address = address;
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

}
