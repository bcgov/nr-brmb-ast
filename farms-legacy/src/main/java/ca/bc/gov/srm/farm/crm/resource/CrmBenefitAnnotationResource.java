package ca.bc.gov.srm.farm.crm.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;

public class CrmBenefitAnnotationResource extends CrmAnnotationResource {

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("objectid_vsi_transactionbenefit@odata.bind")
  public String getObjectid_account() {
    return CrmTransferFormatUtil.formatNavigationPropertyValue(CrmConstants.BENEFIT_UPDATE_ENDPOINT, entityId);
  }

  @Override
  public String toString() {
    return "CrmBenefitAnnotationResource [getSubject()=" + getSubject() + ", getNotetext()=" + getNotetext()
        + ", getFilename()=" + getFilename() + ", getIsdocument()=" + getIsdocument() + ", getDocumentbody()="
        + getDocumentbody() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
        + super.toString() + "]";
  }

}
