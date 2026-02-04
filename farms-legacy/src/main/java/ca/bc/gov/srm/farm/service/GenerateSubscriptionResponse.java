package ca.bc.gov.srm.farm.service;

import java.util.ArrayList;
import java.util.List;


/**
 * Generating a subscription returns serveral things.
 */
public class GenerateSubscriptionResponse {

  /** DOCUMENT ME! */
  private Integer clientSubscriptionId;

  /** DOCUMENT ME! */
  private List errorMessages = new ArrayList();

  /**
   * @return  the clientSubscriptionId
   */
  public Integer getClientSubscriptionId() {
    return clientSubscriptionId;
  }

  /**
   * @param  clientSubscriptionId  the clientSubscriptionId to set
   */
  public void setClientSubscriptionId(Integer clientSubscriptionId) {
    this.clientSubscriptionId = clientSubscriptionId;
  }

  /**
   * @return  the errorMessages
   */
  public List getErrorMessages() {
    return errorMessages;
  }

  /**
   * @param  errorMessages  the errorMessages to set
   */
  public void setErrorMessages(List errorMessages) {
    this.errorMessages = errorMessages;
  }

}
