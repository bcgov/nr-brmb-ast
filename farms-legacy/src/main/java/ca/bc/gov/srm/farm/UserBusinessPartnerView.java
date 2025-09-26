/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

/**
 * UserBusinessPartnerView.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class UserBusinessPartnerView extends UserBasicView {

  /** businessActivationCode. */
  private String businessActivationCode;

  /** businessGuid. */
  private String businessGuid;

  /** businessLegalName. */
  private String businessLegalName;

  /**
   * getBusinessActivationCode.
   *
   * @return  The return value.
   */
  public String getBusinessActivationCode() {
    return businessActivationCode;
  }


  /**
   * getBusinessGuid.
   *
   * @return  The return value.
   */
  public String getBusinessGuid() {
    return businessGuid;
  }

  /**
   * getBusinessLegalName.
   *
   * @return  The return value.
   */
  public String getBusinessLegalName() {
    return businessLegalName;
  }


  /**
   * Sets the value for business activation code.
   *
   * @param  value  businessActivationCode Input parameter.
   */
  public void setBusinessActivationCode(final String value) {
    this.businessActivationCode = value;
  }


  /**
   * Sets the value for business guid.
   *
   * @param  value  businessGuid Input parameter.
   */
  public void setBusinessGuid(final String value) {
    this.businessGuid = value;
  }

  /**
   * Sets the value for business legal name.
   *
   * @param  value  businessLegalName Input parameter.
   */
  public void setBusinessLegalName(final String value) {
    this.businessLegalName = value;
  }

  /**
   * toString.
   *
   * @return  The return value.
   */
  @Override
  public String toString() {
    StringBuffer retValue = new StringBuffer();
    retValue.append(classnameToString()).append("(");
    retValue.append("super=");
    retValue.append(super.toString());
    retValue.append("businessActivationCode=");
    retValue.append(businessActivationCode).append(",");
    retValue.append("businessGuid=");
    retValue.append(businessGuid).append(",");
    retValue.append("businessLegalName=");
    retValue.append(businessLegalName).append(")");

    return retValue.toString();
  }
}
