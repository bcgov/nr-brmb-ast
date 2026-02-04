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

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ca.bc.gov.srm.farm.domain.Client;

/**
 * @author awilkinson
 */
public class CrmAccountResource extends CrmResource {
  
  @JsonInclude(Include.NON_NULL)
  private String accountid;
  
  private String vsi_pin;
  private String name;
  private String vsi_identityeffectivedate;
  private String new_participanttype;
  private String vsi_socialinsurancenumber;
  
  private String vsi_firstname;
  private String vsi_lastname;
  
  private String vsi_businessnumber;
  private String new_farmsmunicipality;
  
  private String telephone1; // Day Phone
  private String telephone2; // Cell Phone
  private String emailaddress1;
  private String fax;
  
  private String address1_line1;
  private String address1_line2;
  private String address1_city;
  private String address1_stateorprovince;
  private String address1_country;
  private String address1_postalcode;
  
  // CRA Contact
  private String address2_name; // First Name
  private String address1_primarycontactname; // Last Name
  private String vsi_company;
  private String address2_telephone1; // Day Phone Number
  private String address2_telephone2; // Evening Phone Number
  private String emailaddress2;
  private String address2_fax;
  private String address2_line1;
  private String address2_line2;
  private String address2_city;
  private String address2_stateorprovince;
  private String address2_country;
  private String address2_postalcode;
  private String address2_county;  // Corporation Name
  
  private Boolean vsi_cashmarginsoptin;
  
  @JsonInclude(JsonInclude.Include.ALWAYS)
  private Date vsi_cashmarginsoptindate;
  
  public String getVsi_pin() {
    return vsi_pin;
  }
  
  public void setVsi_pin(String vsi_pin) {
    this.vsi_pin = vsi_pin;
  }
  
  public String getAccountid() {
    return accountid;
  }
  
  public void setAccountid(String accountid) {
    this.accountid = accountid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVsi_identityeffectivedate() {
    return vsi_identityeffectivedate;
  }

  public void setVsi_identityeffectivedate(String vsi_identityeffectivedate) {
    this.vsi_identityeffectivedate = vsi_identityeffectivedate;
  }

  public String getTelephone1() {
    return telephone1;
  }

  public void setTelephone1(String telephone1) {
    this.telephone1 = telephone1;
  }

  public String getTelephone2() {
    return telephone2;
  }

  public void setTelephone2(String telephone2) {
    this.telephone2 = telephone2;
  }

  public String getEmailaddress1() {
    return emailaddress1;
  }

  public void setEmailaddress1(String emailaddress1) {
    this.emailaddress1 = emailaddress1;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getAddress1_line1() {
    return address1_line1;
  }

  public void setAddress1_line1(String address1_line1) {
    this.address1_line1 = address1_line1;
  }

  public String getAddress1_line2() {
    return address1_line2;
  }

  public void setAddress1_line2(String address1_line2) {
    this.address1_line2 = address1_line2;
  }

  public String getAddress1_city() {
    return address1_city;
  }

  public void setAddress1_city(String address1_city) {
    this.address1_city = address1_city;
  }

  public String getAddress1_stateorprovince() {
    return address1_stateorprovince;
  }

  public void setAddress1_stateorprovince(String address1_stateorprovince) {
    this.address1_stateorprovince = address1_stateorprovince;
  }

  public String getAddress1_country() {
    return address1_country;
  }

  public void setAddress1_country(String address1_country) {
    this.address1_country = address1_country;
  }

  public String getAddress1_postalcode() {
    return address1_postalcode;
  }

  public void setAddress1_postalcode(String address1_postalcode) {
    this.address1_postalcode = address1_postalcode;
  }

  public String getAddress2_name() {
    return address2_name;
  }

  public void setAddress2_name(String address2_name) {
    this.address2_name = address2_name;
  }

  public String getAddress1_primarycontactname() {
    return address1_primarycontactname;
  }

  public void setAddress1_primarycontactname(String address1_primarycontactname) {
    this.address1_primarycontactname = address1_primarycontactname;
  }

  public String getVsi_company() {
    return vsi_company;
  }

  public void setVsi_company(String vsi_company) {
    this.vsi_company = vsi_company;
  }

  public String getAddress2_telephone1() {
    return address2_telephone1;
  }

  public void setAddress2_telephone1(String address2_telephone1) {
    this.address2_telephone1 = address2_telephone1;
  }

  public String getAddress2_telephone2() {
    return address2_telephone2;
  }

  public void setAddress2_telephone2(String address2_telephone2) {
    this.address2_telephone2 = address2_telephone2;
  }

  public String getEmailaddress2() {
    return emailaddress2;
  }

  public void setEmailaddress2(String emailaddress2) {
    this.emailaddress2 = emailaddress2;
  }

  public String getAddress2_fax() {
    return address2_fax;
  }

  public void setAddress2_fax(String address2_fax) {
    this.address2_fax = address2_fax;
  }

  public String getAddress2_line1() {
    return address2_line1;
  }

  public void setAddress2_line1(String address2_line1) {
    this.address2_line1 = address2_line1;
  }

  public String getAddress2_line2() {
    return address2_line2;
  }

  public void setAddress2_line2(String address2_line2) {
    this.address2_line2 = address2_line2;
  }

  public String getAddress2_city() {
    return address2_city;
  }

  public void setAddress2_city(String address2_city) {
    this.address2_city = address2_city;
  }

  public String getAddress2_stateorprovince() {
    return address2_stateorprovince;
  }

  public void setAddress2_stateorprovince(String address2_stateorprovince) {
    this.address2_stateorprovince = address2_stateorprovince;
  }

  public String getAddress2_country() {
    return address2_country;
  }

  public void setAddress2_country(String address2_country) {
    this.address2_country = address2_country;
  }

  public String getAddress2_postalcode() {
    return address2_postalcode;
  }

  public void setAddress2_postalcode(String address2_postalcode) {
    this.address2_postalcode = address2_postalcode;
  }

  public String getNew_participanttype() {
    return new_participanttype;
  }

  public void setNew_participanttype(String new_participanttype) {
    this.new_participanttype = new_participanttype;
  }

  public String getNew_farmsmunicipality() {
    return new_farmsmunicipality;
  }

  public void setNew_farmsmunicipality(String new_farmsmunicipality) {
    this.new_farmsmunicipality = new_farmsmunicipality;
  }

  public String getVsi_socialinsurancenumber() {
    return vsi_socialinsurancenumber;
  }

  public void setVsi_socialinsurancenumber(String vsi_socialinsurancenumber) {
    this.vsi_socialinsurancenumber = vsi_socialinsurancenumber;
  }

  public String getVsi_firstname() {
    return vsi_firstname;
  }

  public void setVsi_firstname(String vsi_firstname) {
    this.vsi_firstname = vsi_firstname;
  }

  public String getVsi_lastname() {
    return vsi_lastname;
  }

  public void setVsi_lastname(String vsi_lastname) {
    this.vsi_lastname = vsi_lastname;
  }

  public String getVsi_businessnumber() {
    return vsi_businessnumber;
  }

  public void setVsi_businessnumber(String vsi_businessnumber) {
    this.vsi_businessnumber = vsi_businessnumber;
  }

  public void setBusinessNumberFromClient(Client client) {
    String farmBn = StringUtils.defaultIfEmpty(client.getTrustNumber(), client.getBusinessNumber());
    if (farmBn != null) {
      farmBn = farmBn.replace(" ", "");
      farmBn = farmBn.substring(0, Math.min(farmBn.length(), 9));
    }
    setVsi_businessnumber(farmBn);
  }

  public String getAddress2_county() {
    return address2_county;
  }

  public void setAddress2_county(String address2_county) {
    this.address2_county = address2_county;
  }

  public Boolean getVsi_cashmarginsoptin() {
    return vsi_cashmarginsoptin;
  }

  public void setVsi_cashmarginsoptin(Boolean vsi_cashmarginsoptin) {
    this.vsi_cashmarginsoptin = vsi_cashmarginsoptin;
  }

  public String getVsi_cashmarginsoptindate() {
    if (vsi_cashmarginsoptindate == null) {
      return null;
    }
    return new SimpleDateFormat("yyyy-MM-dd").format(vsi_cashmarginsoptindate);
  }

  public void setVsi_cashmarginsoptindate(Date vsi_cashmarginsoptindate) {
    this.vsi_cashmarginsoptindate = vsi_cashmarginsoptindate;
  }

  @Override
  public String toString() {
    return "CrmAccountResource [accountid=" + accountid + ", vsi_pin=" + vsi_pin + ", name=" + name + ", vsi_identityeffectivedate="
        + vsi_identityeffectivedate + ", new_participanttype=" + new_participanttype + ", vsi_socialinsurancenumber=" + vsi_socialinsurancenumber
        + ", vsi_businessnumber=" + vsi_businessnumber + ", new_farmsmunicipality=" + new_farmsmunicipality + ", telephone1=" + telephone1
        + ", telephone2=" + telephone2 + ", emailaddress1=" + emailaddress1 + ", fax=" + fax + ", address1_line1=" + address1_line1
        + ", address1_line2=" + address1_line2 + ", address1_city=" + address1_city + ", address1_stateorprovince=" + address1_stateorprovince
        + ", address1_country=" + address1_country + ", address1_postalcode=" + address1_postalcode + ", address2_name=" + address2_name
        + ", address1_primarycontactname=" + address1_primarycontactname + ", vsi_company=" + vsi_company + ", address2_telephone1="
        + address2_telephone1 + ", address2_telephone2=" + address2_telephone2 + ", emailaddress2=" + emailaddress2 + ", address2_fax=" + address2_fax
        + ", address2_line1=" + address2_line1 + ", address2_line2=" + address2_line2 + ", address2_city=" + address2_city
        + ", address2_stateorprovince=" + address2_stateorprovince + ", address2_country=" + address2_country
        + ", vsi_cashmarginsoptin=" + vsi_cashmarginsoptin + ", vsi_cashmarginsoptindate=" + vsi_cashmarginsoptindate
        + ", vsi_firstname=" + vsi_firstname + ", vsi_lastname=" + vsi_lastname + "]";
  }
  
}
