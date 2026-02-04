/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm;

import java.util.Date;


/**
 * Organization.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class Organization extends BaseObject {

  /** description. */
  private String description;

  /** effectiveDate. */
  private Date effectiveDate;

  /** expiryDate. */
  private Date expiryDate;

  /** id. */
  private Long id;

  /** name. */
  private String name;

  /** organizationCode. */
  private String organizationCode;

  /** organizationTypeCode. */
  private String organizationTypeCode;

  /** revisionCount. */
  private Long revisionCount;


  /** Creates a new Organization object. */
  public Organization() {
  }


  /**
   * getDescription.
   *
   * @return  The return value.
   */
  public String getDescription() {
    return description;
  }


  /**
   * getEffectiveDate.
   *
   * @return  The return value.
   */
  public Date getEffectiveDate() {
    return effectiveDate;
  }


  /**
   * getExpiryDate.
   *
   * @return  The return value.
   */
  public Date getExpiryDate() {
    return expiryDate;
  }


  /**
   * getId.
   *
   * @return  The return value.
   */
  public Long getId() {
    return id;
  }


  /**
   * getName.
   *
   * @return  The return value.
   */
  public String getName() {
    return name;
  }


  /**
   * getOrganizationCode.
   *
   * @return  The return value.
   */
  public String getOrganizationCode() {
    return organizationCode;
  }


  /**
   * getOrganizationTypeCode.
   *
   * @return  The return value.
   */
  public String getOrganizationTypeCode() {
    return organizationTypeCode;
  }


  /**
   * getRevisionCount.
   *
   * @return  The return value.
   */
  public Long getRevisionCount() {
    return revisionCount;
  }


  /**
   * Sets the value for description.
   *
   * @param  value  description Input parameter.
   */
  public void setDescription(final String value) {
    this.description = value;
  }


  /**
   * Sets the value for effective date.
   *
   * @param  value  effectiveDate Input parameter.
   */
  public void setEffectiveDate(final Date value) {
    this.effectiveDate = value;
  }


  /**
   * Sets the value for expiry date.
   *
   * @param  value  expiryDate Input parameter.
   */
  public void setExpiryDate(final Date value) {
    this.expiryDate = value;
  }


  /**
   * Sets the value for id.
   *
   * @param  value  id Input parameter.
   */
  public void setId(final Long value) {
    this.id = value;
  }

  /**
   * Sets the value for name.
   *
   * @param  value  name Input parameter.
   */
  public void setName(final String value) {
    this.name = value;
  }


  /**
   * Sets the value for organization code.
   *
   * @param  value  organizationCode Input parameter.
   */
  public void setOrganizationCode(final String value) {
    this.organizationCode = value;
  }


  /**
   * Sets the value for organization type code.
   *
   * @param  value  organizationTypeCode Input parameter.
   */
  public void setOrganizationTypeCode(final String value) {
    this.organizationTypeCode = value;
  }


  /**
   * Sets the value for revision count.
   *
   * @param  value  revisionCount Input parameter.
   */
  public void setRevisionCount(final Long value) {
    this.revisionCount = value;
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
    retValue.append("id=");
    retValue.append(id).append(",");
    retValue.append("name=");
    retValue.append(name).append(",");
    retValue.append("organizationCode=");
    retValue.append(organizationCode).append(")");

    return retValue.toString();
  }

  /**
   * generateHashCode.
   *
   * @return  The return value.
   */
  @Override
  protected int generateHashCode() {
    int result = HASH_SEED;
    result = hash(result, getClass().getName());
    result = hash(result, id);

    return result;
  }
}
