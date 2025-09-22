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

import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.codes.ParticipantClassCodes;
import ca.bc.gov.srm.farm.util.DateUtils;

/**
 * @author awilkinson
 */
public class AccountTransformer {
  
  public CrmAccountResource transformToCrmResource(Client client, FarmingYear farmingYear) {
    CrmAccountResource resource = new CrmAccountResource();
    
    Person owner = client.getOwner();
    Person contact = client.getContact();

    String ownerName;
    if(ParticipantClassCodes.INDIVIDUAL.equals(client.getParticipantClassCode())) {
      ownerName = owner.getLastName() + ", " + owner.getFirstName();
      
    } else { // Corporation
      ownerName = owner.getCorpName();
    }
    resource.setName(ownerName);
    
    String identEffectiveDateString = DateUtils.formatDateTime(client.getIdentEffectiveDate());
    
    resource.setVsi_pin(client.getParticipantPin().toString());
    resource.setVsi_identityeffectivedate(identEffectiveDateString);
    resource.setNew_participanttype(client.getParticipantClassCodeDescription());
    resource.setVsi_socialinsurancenumber(client.getSin());
    resource.setBusinessNumberFromClient(client);
    resource.setNew_farmsmunicipality(farmingYear.getMunicipalityCodeDescription());
    resource.setTelephone1(owner.getDaytimePhone());
    resource.setTelephone2(owner.getCellNumber());
    resource.setEmailaddress1(owner.getEmailAddress());
    resource.setFax(owner.getFaxNumber());
    resource.setAddress1_line1(owner.getAddressLine1());
    resource.setAddress1_line2(owner.getAddressLine2());
    resource.setAddress1_city(owner.getCity());
    resource.setAddress1_stateorprovince(owner.getProvinceState());
    resource.setAddress1_country(owner.getCountry());
    resource.setAddress1_postalcode(owner.getPostalCode());
    resource.setVsi_company(owner.getCorpName());
    
    resource.setVsi_firstname(owner.getFirstName());
    resource.setVsi_lastname(owner.getLastName());
    
    resource.setAddress2_name(contact.getFirstName());
    resource.setAddress1_primarycontactname(contact.getLastName());
    resource.setAddress2_county(contact.getCorpName());
    resource.setAddress2_telephone1(contact.getDaytimePhone());
    resource.setAddress2_telephone2(contact.getEveningPhone());
    resource.setEmailaddress2(contact.getEmailAddress());
    resource.setAddress2_fax(contact.getFaxNumber());
    resource.setAddress2_line1(contact.getAddressLine1());
    resource.setAddress2_line2(contact.getAddressLine2());
    resource.setAddress2_city(contact.getCity());
    resource.setAddress2_stateorprovince(contact.getProvinceState());
    resource.setAddress2_country(contact.getCountry());
    resource.setAddress2_postalcode(contact.getPostalCode());

    return resource;
  }

}
