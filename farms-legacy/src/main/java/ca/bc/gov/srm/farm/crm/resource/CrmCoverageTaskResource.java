/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm.resource;

public class CrmCoverageTaskResource extends CrmTaskResource {

  private String cr4dd_origin;
  private String cr4dd_method;

  private String cr4dd_commoditiesfarmed;
  private String cr4dd_significantchangeorcircumstances;
  private String cr4dd_productioninsurance;
  private Boolean cr4dd_uninsuredcommodities;
  private Boolean cr4dd_monthsoffarmingactivity;
  private Boolean cr4dd_ownershipinterestinanotherfarm;

  private String regardingobjectid;

  public String getCr4dd_origin() {
    return cr4dd_origin;
  }

  public void setCr4dd_origin(String cr4dd_origin) {
    this.cr4dd_origin = cr4dd_origin;
  }

  public String getCr4dd_method() {
    return cr4dd_method;
  }

  public void setCr4dd_method(String cr4dd_method) {
    this.cr4dd_method = cr4dd_method;
  }

  public String getCr4dd_commoditiesfarmed() {
    return cr4dd_commoditiesfarmed;
  }

  public void setCr4dd_commoditiesfarmed(String cr4dd_commoditiesfarmed) {
    this.cr4dd_commoditiesfarmed = cr4dd_commoditiesfarmed;
  }

  public String getCr4dd_significantchangeorcircumstances() {
    return cr4dd_significantchangeorcircumstances;
  }

  public void setCr4dd_significantchangeorcircumstances(String cr4dd_significantchangeorcircumstances) {
    this.cr4dd_significantchangeorcircumstances = cr4dd_significantchangeorcircumstances;
  }

  public String getCr4dd_productioninsurance() {
    return cr4dd_productioninsurance;
  }

  public void setCr4dd_productioninsurance(String cr4dd_productioninsurance) {
    this.cr4dd_productioninsurance = cr4dd_productioninsurance;
  }

  public Boolean getCr4dd_uninsuredcommodities() {
    return cr4dd_uninsuredcommodities;
  }

  public void setCr4dd_uninsuredcommodities(Boolean cr4dd_uninsuredcommodities) {
    this.cr4dd_uninsuredcommodities = cr4dd_uninsuredcommodities;
  }

  public Boolean getCr4dd_monthsoffarmingactivity() {
    return cr4dd_monthsoffarmingactivity;
  }

  public void setCr4dd_monthsoffarmingactivity(Boolean cr4dd_monthsoffarmingactivity) {
    this.cr4dd_monthsoffarmingactivity = cr4dd_monthsoffarmingactivity;
  }

  public Boolean getCr4dd_ownershipinterestinanotherfarm() {
    return cr4dd_ownershipinterestinanotherfarm;
  }

  public void setCr4dd_ownershipinterestinanotherfarm(Boolean cr4dd_ownershipinterestinanotherfarm) {
    this.cr4dd_ownershipinterestinanotherfarm = cr4dd_ownershipinterestinanotherfarm;
  }

  public String getRegardingobjectid() {
    return regardingobjectid;
  }

  public void setRegardingobjectid(String regardingobjectid) {
    this.regardingobjectid = regardingobjectid;
  }

}
