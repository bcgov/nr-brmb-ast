/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.list;


/**
 * @author awilkinson
 * @created Jan 6, 2011
 */
public class LineItemListView extends BaseListView {
  
  private Integer lineItem;

  private String description;

  /**
   * eligible identifies if the is eligible for either the
   * program year or at least one of the reference years.
   * Controls whether or not the line item can be found in
   * the auto-complete boxes when displaying Eligible items. 
   */
  private Boolean eligible;
  
  /**
   * ineligible identifies if the is eligible for either the
   * program year or at least one of the reference years.
   * Controls whether or not the line item can be found in
   * the auto-complete boxes when displaying Ineligible items. 
   */
  private Boolean ineligible;
  

  /**
   * @param lineItem LineItem
   */
  public LineItemListView() {
    super();
  }

  /**
   * @return String
   */
  @Override
  public String getLabel() {
    return description;
  }

  /**
   * @return String
   */
  @Override
  public String getValue() {
    return lineItem.toString();
  }

  /**
   * @return the lineItem
   */
  public Integer getLineItem() {
    return lineItem;
  }

  /**
   * @param lineItem the lineItem to set
   */
  public void setLineItem(Integer lineItem) {
    this.lineItem = lineItem;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the eligible
   */
  public Boolean getEligible() {
    return eligible;
  }

  /**
   * @param eligible the eligible to set
   */
  public void setEligible(Boolean eligible) {
    this.eligible = eligible;
  }

  /**
   * @return the ineligible
   */
  public Boolean getIneligible() {
    return ineligible;
  }

  /**
   * @param ineligible the ineligible to set
   */
  public void setIneligible(Boolean ineligible) {
    this.ineligible = ineligible;
  }
}
