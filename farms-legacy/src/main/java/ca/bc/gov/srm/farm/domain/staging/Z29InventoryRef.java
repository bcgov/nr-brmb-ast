
package ca.bc.gov.srm.farm.domain.staging;

/**
 * Z29InventoryRef identifies a list of inventory (commodity) codes, and their
 * associted descriptions used by FIPD. This file is created by FIPD. This is a
 * staging object used to load temporary data set before being merged into the
 * operational data
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:59:40 PM
 */
public final class Z29InventoryRef {

  /**
   * inventoryCode is a numeric code used to uniquely identify an inventory
   * item.
   */
  private java.lang.Integer inventoryCode;

  /**
   * inventoryTypeCode is a numeric code indicating an inventory type. Valid
   * values are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased
   * Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.
   */
  private java.lang.Integer inventoryTypeCode;

  /**
   * inventoryDesc is the english description of the Inventory. An English text
   * description of an inventory item.
   */
  private String inventoryDesc;

  /** inventoryTypeDescription is a description of the inventory type. */
  private String inventoryTypeDescription;

  /** inventoryGroupCode is a number assigned to a group of inventory items. */
  private java.lang.Integer inventoryGroupCode;

  /**
   * inventoryGroupDescription is a description for the inventory group code.
   */
  private String inventoryGroupDescription;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private java.lang.Integer revisionCount;
  
  /**
   * Crop and livestock inventories for market commodities are valued using both an 
   * opening price (P1) and a year-end price (P2). Breeding animals and culled breeding 
   * animals, which are not considered market commodities, are valued using a yearend 
   * (P2) price only.  NOTE: Added by BC.
   */
  private java.lang.Boolean marketCommodityInd;

  /** The Z40PrtcpntRefSuplDtl associated with this Z29InventoryRef. */
  private Z40PrtcpntRefSuplDtl[] z40PrtcpntRefSuplDtl;

  /** The Z21ParticipantSuppl associated with this Z29InventoryRef. */
  private Z21ParticipantSuppl[] z21ParticipantSuppl;

  /** Constructor. */
  public Z29InventoryRef() {

  }

  /**
   * @return  Z21ParticipantSuppl[]
   */
  public Z21ParticipantSuppl[] getZ21ParticipantSuppl() {
    return z21ParticipantSuppl;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setZ21ParticipantSuppl(final Z21ParticipantSuppl[] newVal) {
    z21ParticipantSuppl = newVal;
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
   * InventoryCode is a numeric code used to uniquely identify an inventory
   * item.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getInventoryCode() {
    return inventoryCode;
  }

  /**
   * InventoryCode is a numeric code used to uniquely identify an inventory
   * item.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryCode(final java.lang.Integer newVal) {
    inventoryCode = newVal;
  }

  /**
   * InventoryTypeCode is a numeric code indicating an inventory type. Valid
   * values are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased
   * Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getInventoryTypeCode() {
    return inventoryTypeCode;
  }

  /**
   * InventoryTypeCode is a numeric code indicating an inventory type. Valid
   * values are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased
   * Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryTypeCode(final java.lang.Integer newVal) {
    inventoryTypeCode = newVal;
  }

  /**
   * InventoryDesc is the english description of the Inventory. An English text
   * description of an inventory item.
   *
   * @return  String
   */
  public String getInventoryDesc() {
    return inventoryDesc;
  }

  /**
   * InventoryDesc is the english description of the Inventory. An English text
   * description of an inventory item.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryDesc(final String newVal) {
    inventoryDesc = newVal;
  }

  /**
   * InventoryTypeDescription is a description of the inventory type.
   *
   * @return  String
   */
  public String getInventoryTypeDescription() {
    return inventoryTypeDescription;
  }

  /**
   * InventoryTypeDescription is a description of the inventory type.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryTypeDescription(final String newVal) {
    inventoryTypeDescription = newVal;
  }

  /**
   * InventoryGroupCode is a number assigned to a group of inventory items.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getInventoryGroupCode() {
    return inventoryGroupCode;
  }

  /**
   * InventoryGroupCode is a number assigned to a group of inventory items.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryGroupCode(final java.lang.Integer newVal) {
    inventoryGroupCode = newVal;
  }

  /**
   * InventoryGroupDescription is a description for the inventory group code.
   *
   * @return  String
   */
  public String getInventoryGroupDescription() {
    return inventoryGroupDescription;
  }

  /**
   * InventoryGroupDescription is a description for the inventory group code.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryGroupDescription(final String newVal) {
    inventoryGroupDescription = newVal;
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

  /**
   * Gets marketCommodityInd
   *
   * @return the marketCommodityInd
   */
  public java.lang.Boolean getMarketCommodityInd() {
    return marketCommodityInd;
  }

  /**
   * Sets marketCommodityInd
   *
   * @param pMarketCommodityInd the marketCommodityInd to set
   */
  public void setMarketCommodityInd(java.lang.Boolean pMarketCommodityInd) {
    marketCommodityInd = pMarketCommodityInd;
  }

}
