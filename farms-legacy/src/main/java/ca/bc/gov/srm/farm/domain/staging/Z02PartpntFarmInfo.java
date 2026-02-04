
package ca.bc.gov.srm.farm.domain.staging;

/**
 * Z02PartpntFarmInfo lists AgriStablity Participant Farming information, from
 * page 1/Section 1 of the Harmonized t1273 form. This file is sent to the
 * provinces by FIPD. This is a staging object used to load temporary data set
 * before being merged into the operational data
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:07:05 PM
 */
public final class Z02PartpntFarmInfo {

  /**
   * participantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   */
  private java.lang.Integer participantPin;

  /** programYear is the stabilization year for this record. */
  private java.lang.Integer programYear;

  /**
   * formVersionNumber Distinguishes between different versions of the
   * AgriStability application from the producer. Both the producer and the
   * administration can initiate adjustments that create a new form version in a
   * specific ProgramYear.
   */
  private java.lang.Integer formVersionNumber;

  /** provinceOfResidence is the province of Residence. */
  private String provinceOfResidence;

  /**
   * provinceOfMainFarmstead is province of Main Farmstead from page 1 of T1163
   * E.
   */
  private String provinceOfMainFarmstead;

  /**
   * postmarkDate is the Date Form was Postmarked. Will be received date if
   * received before filing deadline.
   */
  private String postmarkDate;

  /** receivedDate is the date form was received by RCT. */
  private String receivedDate;

  /**
   * isSoleProprietor is Y if carried on farming business as a sole proprietor,
   * otherwise N. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isSoleProprietor;

  /**
   * isPartnershipMember is Y if carried on farming business partner of a
   * partnership; otherwise N. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isPartnershipMember;

  /**
   * isCorporateShareholder is Y if carried on farming business as a shareholder
   * of a corporation; otherwise N. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isCorporateShareholder;

  /**
   * isCoopMember is Y if carried on farming business as a member of a
   * co-operative, otherwise N.
   */
  private java.lang.Boolean isCoopMember;

  /** commonShareTotal denoted the the outstanding common shares. */
  private java.lang.Integer commonShareTotal;

  /** farmYears is the number of farming years. */
  private java.lang.Integer farmYears;

  /**
   * isLastYearFarming was the last year of farming. Allowable values are Y -
   * Yes, N - No.
   */
  private java.lang.Boolean isLastYearFarming;

  /** formOriginCode indicates how the form information was received at CCRA. */
  private java.lang.Integer formOriginCode;

  /** industryCode is the code for the industry. */
  private java.lang.Integer industryCode;

  /**
   * participantProfileCode is a code to indicate which programs the participant
   * is applying for.
   */
  private java.lang.Integer participantProfileCode;

  /**
   * isAccrualCashConversion indicates if the accrual to cash/cash to accrual
   * conversions box is checked. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isAccrualCashConversion;

  /**
   * isPerishableCommodities indicates if the Perishable Commodities Worksheet
   * box checked. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isPerishableCommodities;

  /**
   * isReceipts indicates if the receipts box is checked. Allowable values are Y
   * - Yes, N - No.
   */
  private java.lang.Boolean isReceipts;

  /**
   * isOtherText identifies if Other Text ind is checked. Allowable values are Y
   * - Yes, N - No.
   */
  private java.lang.Boolean isOtherText;

  /** otherText contains the additional text if the other box is checked. */
  private String otherText;

  /**
   * isAccrualWorksheet indicates if the Accrual Reference Margin Worksheet box
   * is checked. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isAccrualWorksheet;

  /**
   * isCwbWorksheet indicates if the CWB Adjustment Worksheet box is checked.
   * Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isCwbWorksheet;

  /**
   * isCombinedThisYear indicates if the "Should this operation be combined for"
   * box is checked. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isCombinedThisYear;

  /**
   * isCompletedProdCycle indicates if the "Have you completed a production
   * cycle on at least one of the commodities you produced?" box is checked.
   * Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isCompletedProdCycle;

  /**
   * isDisaster indicates if the "were you unable to complete a production cycle
   * due to disaster circumstances" box is checked. Allowable values are Y -
   * Yes, N - No.
   */
  private java.lang.Boolean isDisaster;

  /**
   * isCopyCobToContact can be "Y" or "N" - "Y" indicates that a copy of the
   * Calculation of Benefits (COB) statement should be sent to the Contact
   * person. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isCopyCobToContact;

  /** municipalityCode is the three digit code for the Municipality. */
  private java.lang.Integer municipalityCode;

  /**
   * formVersionEffectiveDate is the date the form version information was last updated.
   */
  private String formVersionEffectiveDate;
  
  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private java.lang.Integer revisionCount;

  /** The Z03StatementInfo associated with this Z02PartpntFarmInfo. */
  private Z03StatementInfo[] z03StatementInfo;

  /** The Z42ParticipantRefYear associated with this Z02PartpntFarmInfo. */
  private Z42ParticipantRefYear[] z42ParticipantRefYear;

  /** The Z50ParticipntBnftCalc associated with this Z02PartpntFarmInfo. */
  private Z50ParticipntBnftCalc[] z50ParticipntBnftCalc;

  /** The Z51ParticipantContrib associated with this Z02PartpntFarmInfo. */
  private Z51ParticipantContrib[] z51ParticipantContrib;

  /** Constructor. */

  public Z02PartpntFarmInfo() {

  }

  /**
   * @return  Z03StatementInfo[]
   */
  public Z03StatementInfo[] getZ03StatementInfo() {
    return z03StatementInfo;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setZ03StatementInfo(final Z03StatementInfo[] newVal) {
    z03StatementInfo = newVal;
  }

  /**
   * @return  Z42ParticipantRefYear[]
   */
  public Z42ParticipantRefYear[] getZ42ParticipantRefYear() {
    return z42ParticipantRefYear;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setZ42ParticipantRefYear(final Z42ParticipantRefYear[] newVal) {
    z42ParticipantRefYear = newVal;
  }

  /**
   * @return  Z50ParticipntBnftCalc[]
   */
  public Z50ParticipntBnftCalc[] getZ50ParticipntBnftCalc() {
    return z50ParticipntBnftCalc;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setZ50ParticipntBnftCalc(final Z50ParticipntBnftCalc[] newVal) {
    z50ParticipntBnftCalc = newVal;
  }

  /**
   * @return  Z51ParticipantContrib[]
   */
  public Z51ParticipantContrib[] getZ51ParticipantContrib() {
    return z51ParticipantContrib;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setZ51ParticipantContrib(final Z51ParticipantContrib[] newVal) {
    z51ParticipantContrib = newVal;
  }

  /**
   * ParticipantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getParticipantPin() {
    return participantPin;
  }

  /**
   * ParticipantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantPin(final java.lang.Integer newVal) {
    participantPin = newVal;
  }

  /**
   * ProgramYear is the stabilization year for this record.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getProgramYear() {
    return programYear;
  }

  /**
   * ProgramYear is the stabilization year for this record.
   *
   * @param  newVal  The new value for this property
   */
  public void setProgramYear(final java.lang.Integer newVal) {
    programYear = newVal;
  }

  /**
   * FormVersionNumber Distinguishes between different versions of the
   * AgriStability application from the producer. Both the producer and the
   * administration can initiate adjustments that create a new form version in a
   * specific ProgramYear.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getFormVersionNumber() {
    return formVersionNumber;
  }

  /**
   * FormVersionNumber Distinguishes between different versions of the
   * AgriStability application from the producer. Both the producer and the
   * administration can initiate adjustments that create a new form version in a
   * specific ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setFormVersionNumber(final java.lang.Integer newVal) {
    formVersionNumber = newVal;
  }

  /**
   * ProvinceOfResidence is the province of Residence.
   *
   * @return  String
   */
  public String getProvinceOfResidence() {
    return provinceOfResidence;
  }

  /**
   * ProvinceOfResidence is the province of Residence.
   *
   * @param  newVal  The new value for this property
   */
  public void setProvinceOfResidence(final String newVal) {
    provinceOfResidence = newVal;
  }

  /**
   * ProvinceOfMainFarmstead is province of Main Farmstead from page 1 of T1163
   * E.
   *
   * @return  String
   */
  public String getProvinceOfMainFarmstead() {
    return provinceOfMainFarmstead;
  }

  /**
   * ProvinceOfMainFarmstead is province of Main Farmstead from page 1 of T1163
   * E.
   *
   * @param  newVal  The new value for this property
   */
  public void setProvinceOfMainFarmstead(final String newVal) {
    provinceOfMainFarmstead = newVal;
  }

  /**
   * PostmarkDate is the Date Form was Postmarked. Will be received date if
   * received before filing deadline.
   *
   * @return  String
   */
  public String getPostmarkDate() {
    return postmarkDate;
  }

  /**
   * PostmarkDate is the Date Form was Postmarked. Will be received date if
   * received before filing deadline.
   *
   * @param  newVal  The new value for this property
   */
  public void setPostmarkDate(final String newVal) {
    postmarkDate = newVal;
  }

  /**
   * ReceivedDate is the date form was received by RCT.
   *
   * @return  String
   */
  public String getReceivedDate() {
    return receivedDate;
  }

  /**
   * ReceivedDate is the date form was received by RCT.
   *
   * @param  newVal  The new value for this property
   */
  public void setReceivedDate(final String newVal) {
    receivedDate = newVal;
  }

  /**
   * IsSoleProprietor is Y if carried on farming business as a sole proprietor,
   * otherwise N. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isSoleProprietor() {
    return isSoleProprietor;
  }

  /**
   * IsSoleProprietor is Y if carried on farming business as a sole proprietor,
   * otherwise N. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsSoleProprietor(final java.lang.Boolean newVal) {
    isSoleProprietor = newVal;
  }

  /**
   * IsPartnershipMember is Y if carried on farming business partner of a
   * partnership; otherwise N. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isPartnershipMember() {
    return isPartnershipMember;
  }

  /**
   * IsPartnershipMember is Y if carried on farming business partner of a
   * partnership; otherwise N. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsPartnershipMember(final java.lang.Boolean newVal) {
    isPartnershipMember = newVal;
  }

  /**
   * IsCorporateShareholder is Y if carried on farming business as a shareholder
   * of a corporation; otherwise N. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isCorporateShareholder() {
    return isCorporateShareholder;
  }

  /**
   * IsCorporateShareholder is Y if carried on farming business as a shareholder
   * of a corporation; otherwise N. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCorporateShareholder(final java.lang.Boolean newVal) {
    isCorporateShareholder = newVal;
  }

  /**
   * IsCoopMember is Y if carried on farming business as a member of a
   * co-operative, otherwise N.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isCoopMember() {
    return isCoopMember;
  }

  /**
   * IsCoopMember is Y if carried on farming business as a member of a
   * co-operative, otherwise N.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCoopMember(final java.lang.Boolean newVal) {
    isCoopMember = newVal;
  }

  /**
   * CommonShareTotal denoted the the outstanding common shares.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getCommonShareTotal() {
    return commonShareTotal;
  }

  /**
   * CommonShareTotal denoted the the outstanding common shares.
   *
   * @param  newVal  The new value for this property
   */
  public void setCommonShareTotal(final java.lang.Integer newVal) {
    commonShareTotal = newVal;
  }

  /**
   * FarmYears is the number of farming years.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getFarmYears() {
    return farmYears;
  }

  /**
   * FarmYears is the number of farming years.
   *
   * @param  newVal  The new value for this property
   */
  public void setFarmYears(final java.lang.Integer newVal) {
    farmYears = newVal;
  }

  /**
   * IsLastYearFarming was the last year of farming. Allowable values are Y -
   * Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isLastYearFarming() {
    return isLastYearFarming;
  }

  /**
   * IsLastYearFarming was the last year of farming. Allowable values are Y -
   * Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsLastYearFarming(final java.lang.Boolean newVal) {
    isLastYearFarming = newVal;
  }

  /**
   * FormOriginCode indicates how the form information was received at CCRA.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getFormOriginCode() {
    return formOriginCode;
  }

  /**
   * FormOriginCode indicates how the form information was received at CCRA.
   *
   * @param  newVal  The new value for this property
   */
  public void setFormOriginCode(final java.lang.Integer newVal) {
    formOriginCode = newVal;
  }

  /**
   * IndustryCode is the code for the industry.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getIndustryCode() {
    return industryCode;
  }

  /**
   * IndustryCode is the code for the industry.
   *
   * @param  newVal  The new value for this property
   */
  public void setIndustryCode(final java.lang.Integer newVal) {
    industryCode = newVal;
  }

  /**
   * ParticipantProfileCode is a code to indicate which programs the participant
   * is applying for.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getParticipantProfileCode() {
    return participantProfileCode;
  }

  /**
   * ParticipantProfileCode is a code to indicate which programs the participant
   * is applying for.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantProfileCode(final java.lang.Integer newVal) {
    participantProfileCode = newVal;
  }

  /**
   * IsAccrualCashConversion indicates if the accrual to cash/cash to accrual
   * conversions box is checked. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isAccrualCashConversion() {
    return isAccrualCashConversion;
  }

  /**
   * IsAccrualCashConversion indicates if the accrual to cash/cash to accrual
   * conversions box is checked. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsAccrualCashConversion(final java.lang.Boolean newVal) {
    isAccrualCashConversion = newVal;
  }

  /**
   * IsPerishableCommodities indicates if the Perishable Commodities Worksheet
   * box checked. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isPerishableCommodities() {
    return isPerishableCommodities;
  }

  /**
   * IsPerishableCommodities indicates if the Perishable Commodities Worksheet
   * box checked. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsPerishableCommodities(final java.lang.Boolean newVal) {
    isPerishableCommodities = newVal;
  }

  /**
   * IsReceipts indicates if the receipts box is checked. Allowable values are Y
   * - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isReceipts() {
    return isReceipts;
  }

  /**
   * IsReceipts indicates if the receipts box is checked. Allowable values are Y
   * - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsReceipts(final java.lang.Boolean newVal) {
    isReceipts = newVal;
  }

  /**
   * IsOtherText identifies if Other Text ind is checked. Allowable values are Y
   * - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isOtherText() {
    return isOtherText;
  }

  /**
   * IsOtherText identifies if Other Text ind is checked. Allowable values are Y
   * - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsOtherText(final java.lang.Boolean newVal) {
    isOtherText = newVal;
  }

  /**
   * OtherText contains the additional text if the other box is checked.
   *
   * @return  String
   */
  public String getOtherText() {
    return otherText;
  }

  /**
   * OtherText contains the additional text if the other box is checked.
   *
   * @param  newVal  The new value for this property
   */
  public void setOtherText(final String newVal) {
    otherText = newVal;
  }

  /**
   * IsAccrualWorksheet indicates if the Accrual Reference Margin Worksheet box
   * is checked. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isAccrualWorksheet() {
    return isAccrualWorksheet;
  }

  /**
   * IsAccrualWorksheet indicates if the Accrual Reference Margin Worksheet box
   * is checked. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsAccrualWorksheet(final java.lang.Boolean newVal) {
    isAccrualWorksheet = newVal;
  }

  /**
   * IsCwbWorksheet indicates if the CWB Adjustment Worksheet box is checked.
   * Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isCwbWorksheet() {
    return isCwbWorksheet;
  }

  /**
   * IsCwbWorksheet indicates if the CWB Adjustment Worksheet box is checked.
   * Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCwbWorksheet(final java.lang.Boolean newVal) {
    isCwbWorksheet = newVal;
  }

  /**
   * IsCombinedThisYear indicates if the "Should this operation be combined for"
   * box is checked. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isCombinedThisYear() {
    return isCombinedThisYear;
  }

  /**
   * IsCombinedThisYear indicates if the "Should this operation be combined for"
   * box is checked. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCombinedThisYear(final java.lang.Boolean newVal) {
    isCombinedThisYear = newVal;
  }

  /**
   * IsCompletedProdCycle indicates if the "Have you completed a production
   * cycle on at least one of the commodities you produced?" box is checked.
   * Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isCompletedProdCycle() {
    return isCompletedProdCycle;
  }

  /**
   * IsCompletedProdCycle indicates if the "Have you completed a production
   * cycle on at least one of the commodities you produced?" box is checked.
   * Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCompletedProdCycle(final java.lang.Boolean newVal) {
    isCompletedProdCycle = newVal;
  }

  /**
   * IsDisaster indicates if the "were you unable to complete a production cycle
   * due to disaster circumstances" box is checked. Allowable values are Y -
   * Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isDisaster() {
    return isDisaster;
  }

  /**
   * IsDisaster indicates if the "were you unable to complete a production cycle
   * due to disaster circumstances" box is checked. Allowable values are Y -
   * Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsDisaster(final java.lang.Boolean newVal) {
    isDisaster = newVal;
  }

  /**
   * IsCopyCobToContact can be "Y" or "N" - "Y" indicates that a copy of the
   * Calculation of Benefits (COB) statement should be sent to the Contact
   * person. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isCopyCobToContact() {
    return isCopyCobToContact;
  }

  /**
   * IsCopyCobToContact can be "Y" or "N" - "Y" indicates that a copy of the
   * Calculation of Benefits (COB) statement should be sent to the Contact
   * person. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCopyCobToContact(final java.lang.Boolean newVal) {
    isCopyCobToContact = newVal;
  }

  /**
   * MunicipalityCode is the three digit code for the Municipality.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getMunicipalityCode() {
    return municipalityCode;
  }

  /**
   * MunicipalityCode is the three digit code for the Municipality.
   *
   * @param  newVal  The new value for this property
   */
  public void setMunicipalityCode(final java.lang.Integer newVal) {
    municipalityCode = newVal;
  }

  /**
   * formVersionEffectiveDate is the date the form version information was last updated.
   *
   * @return  String
   */
  public String getFormVersionEffectiveDate() {
    return formVersionEffectiveDate;
  }

  /**
   * formVersionEffectiveDate is the date the form version information was last updated.
   *
   * @param  formVersionEffectiveDate  The new value for this property
   */
  public void setFormVersionEffectiveDate(String formVersionEffectiveDate) {
    this.formVersionEffectiveDate = formVersionEffectiveDate;
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
