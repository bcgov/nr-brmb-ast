
package ca.bc.gov.srm.farm.domain.staging;

/**
 * Z28ProdInsuranceRef identifies the reference file containing a list of the
 * units of measure, and associated descriptions. This file is created by FIPD.
 * This is a staging object used to load temporary data set before being merged
 * into the operational data
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:07:15 PM
 */
public final class Z28ProdInsuranceRef {

  /** productionUnit is the unit of measure code. */
  private java.lang.Integer productionUnit;

  /** productionUnitDescription is the unit of measure description. */
  private String productionUnitDescription;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private java.lang.Integer revisionCount;

  /** The Z40PrtcpntRefSuplDtl associated with this Z28ProdInsuranceRef. */
  private Z40PrtcpntRefSuplDtl[] z40PrtcpntRefSuplDtl;

  /** Constructor. */
  public Z28ProdInsuranceRef() {

  }

  /**
   * @return  Z40PrtcpntRefSuplDtl[]
   */
  public Z40PrtcpntRefSuplDtl[] getZ40PrtcpntRefSuplDtl() {
    return z40PrtcpntRefSuplDtl;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setZ40PrtcpntRefSuplDtl(final Z40PrtcpntRefSuplDtl[] newVal) {
    z40PrtcpntRefSuplDtl = newVal;
  }

  /**
   * ProductionUnit is the unit of measure code.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getProductionUnit() {
    return productionUnit;
  }

  /**
   * ProductionUnit is the unit of measure code.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductionUnit(final java.lang.Integer newVal) {
    productionUnit = newVal;
  }

  /**
   * ProductionUnitDescription is the unit of measure description.
   *
   * @return  String
   */
  public String getProductionUnitDescription() {
    return productionUnitDescription;
  }

  /**
   * ProductionUnitDescription is the unit of measure description.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductionUnitDescription(final String newVal) {
    productionUnitDescription = newVal;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final java.lang.Integer newVal) {
    revisionCount = newVal;
  }

}
