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
package ca.bc.gov.srm.farm.crm.transform;

import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class AccountUpdateTransformer {
  
  public static final String REGARDING_FIELD_FORMAT = "/accounts(%s)";
  
  public static final String CRA_HEADER = "The CRA has received updated information for this account or its CRA contact.";
  public static final String CHEFS_HEADER = "Updated information for this account and CRA contact has been received from a CHEFS NPP form.";

  private static final String DESCRIPTION_FORMAT =
      "%s\n\n"
      + "ACCOUNT INFO\n"
      + "Identify Effective Date: %s\n"
      + "Participant Type: %s\n"
      + "Corporation Name: %s\n"
      + "First Name: %s\n"
      + "Last Name: %s\n"
      + "SIN: %s\n"
      + "Business Number: %s\n"
      + "Trust Number: %s\n"
      + "Address 1: %s\n"
      + "Address 2: %s\n"
      + "City: %s\n"
      + "Province: %s\n"
      + "Postal Code: %s\n"
      + "Country: %s\n"
      + "Phone (Fax): %s\n"
      + "Phone (Day): %s\n"
      + "Phone (Evening): %s\n"
      + "Phone (Cell): %s\n"
      + "Email Address: %s\n"
      + "\n"
      + "CONTACT INFO\n"
      + "Business Name: %s\n"
      + "First Name: %s\n"
      + "Last Name: %s\n"
      + "Address 1: %s\n"
      + "Address 2: %s\n"
      + "City: %s\n"
      + "Province: %s\n"
      + "Postal Code: %s\n"
      + "Country: %s\n"
      + "Phone: %s\n"
      + "Phone (Fax): %s\n"
      + "Phone (Cell): %s\n"
      + "Email Address: %s\n";

  public CrmTaskResource transformToTask(Client client, String accountId, String submissionGuid) {
    CrmTaskResource resource = new CrmTaskResource();
    
    Person owner = client.getOwner();
    Person contact = client.getContact();
    
    String header = CRA_HEADER;
    if (submissionGuid != null) {
      header = CHEFS_HEADER;
    }
    
    String description = StringUtils.formatWithNullAsEmptyString(DESCRIPTION_FORMAT,
        header,
        DateUtils.formatDate(client.getIdentEffectiveDate()),
        client.getParticipantClassCodeDescription(),
        owner.getCorpName(),
        owner.getFirstName(),
        owner.getLastName(),
        client.getSin(),
        client.getBusinessNumber(),
        client.getTrustNumber(),
        owner.getAddressLine1(),
        owner.getAddressLine2(),
        owner.getCity(),
        owner.getProvinceState(),
        owner.getPostalCode(),
        owner.getCountry(),
        owner.getFaxNumber(),
        owner.getDaytimePhone(),
        owner.getEveningPhone(),
        owner.getCellNumber(),
        owner.getEmailAddress(),
        
        contact.getCorpName(),
        contact.getFirstName(),
        contact.getLastName(),
        contact.getAddressLine1(),
        contact.getAddressLine2(),
        contact.getCity(),
        contact.getProvinceState(),
        contact.getPostalCode(),
        contact.getCountry(),
        contact.getDaytimePhone(),
        contact.getFaxNumber(),
        contact.getCellNumber(),
        contact.getEmailAddress());

    resource.setAccountIdParameter(accountId);
    resource.setSubject("Account or contact update");
    resource.setDescription(description);

    return resource;
  }

}
