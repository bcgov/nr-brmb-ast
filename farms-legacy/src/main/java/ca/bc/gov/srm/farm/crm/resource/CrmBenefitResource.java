/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;

public class CrmBenefitResource extends CrmResource {

  private String vsi_benefitid;

  @JsonProperty("_vsi_participantid_value")
  private String vsi_participantid_value;

  @JsonProperty("vsi_participantprogramyearid")
  private CrmEnrolmentResource vsiParticipantProgramYear;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("vsi_benefit_vsi_NOLTask@odata.bind")
  public String getBenefitNolTask() {
    return CrmTransferFormatUtil.formatNavigationPropertyValue(CrmConstants.ACCOUNT_ENDPOINT, vsi_benefitid);
  }

  public String getVsi_benefitid() {
    return vsi_benefitid;
  }

  public void setVsi_benefitid(String vsi_benefitid) {
    this.vsi_benefitid = vsi_benefitid;
  }

  public String getVsi_participantid_value() {
    return vsi_participantid_value;
  }

  public void setVsi_participantid_value(String vsi_participantid_value) {
    this.vsi_participantid_value = vsi_participantid_value;
  }

  public CrmEnrolmentResource getVsiParticipantProgramYear() {
    return vsiParticipantProgramYear;
  }

  public void setVsiParticipantProgramYear(CrmEnrolmentResource participantProgramYear) {
    this.vsiParticipantProgramYear = participantProgramYear;
  }

  @Override
  public String toString() {
    return "CrmBenefitResource [vsi_benefitid=" + vsi_benefitid + ", vsiParticipantProgramYear=" + vsiParticipantProgramYear.toString() + "]";
  }

}
