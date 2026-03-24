package ca.bc.gov.farms.persistence.v1.dto.staging;

/**
 * Z03StatementInfo identifies the data from section 3 of the Statement A. Also
 * included are the amounts from Section 6 - Summary of Income and Expense.
 * There will be 1 row in this file per participant, per statement A/B filled
 * out. This file is sent to the provinces by FIPD. This is a staging object
 * used to load temporary data set before being merged into the operational data
 *
 * @author Vivid Solutions Inc.
 * @version 1.0
 * @created 03-Jul-2009 2:07:07 PM
 */
public final class Z03StatementInfo {

    /**
     * participantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     */
    private java.lang.Integer participantPin;

    /** programYear is the stabilization year for this record. */
    private java.lang.Integer programYear;

    /**
     * operationNumber identifies each operation a participant reports for a given
     * stab year. Operations may have different statement numbers in different
     * program years.
     */
    private java.lang.Integer operationNumber;

    /**
     * partnershipPin uniquely identifies the partnership. If both the partners in
     * an operation file applications, the same partnershipPin will show up under
     * both pins. partnershipPin will represent the same operation if/when they
     * are used in different program years.
     */
    private java.lang.Integer partnershipPin;

    /** partnershipName is the name of the partnership. */
    private String partnershipName;

    /**
     * partnershipPercent is the percentage of the partnership. (100% will be
     * stored as 1.0).
     */
    private java.lang.Double partnershipPercent;

    /**
     * fiscalYearStart is the Operation Fiscal year Start (yyyymmdd - may be
     * blank).
     */
    private String fiscalYearStart;

    /** fiscalYearEnd is the Operation fiscalYearEnd (yyyymmdd - may be blank). */
    private String fiscalYearEnd;

    /** accountingCode is the Method of Accounting to CRA and AAFC. */
    private java.lang.Integer accountingCode;

    /**
     * isLandlord indicates if this Participant participated in a crop share as a
     * landlord. Allowable values are Y - Yes, N - No.
     */
    private java.lang.Boolean isLandlord;

    /**
     * isCropShare indicates if this Participant participated in a crop share as a
     * tenant. Allowable values are Y - Yes, N - No.
     */
    private java.lang.Boolean isCropShare;

    /**
     * isFeederMember indicates if this participant was a member of a feeder
     * association. Allowable values are Y - Yes, N - No.
     */
    private java.lang.Boolean isFeederMember;

    /** grossIncome is the grossIncome (9959). */
    private java.lang.Double grossIncome;

    /** expenses is the Total Farming Expenses (9968). */
    private java.lang.Double expenses;

    /** netIncomeBeforeAdj is the Net Income before Adjustments (9969). */
    private java.lang.Double netIncomeBeforeAdj;

    /**
     * otherDeductions is the otherDeductions to the net income amount. (9940).
     */
    private java.lang.Double otherDeductions;

    /**
     * inventoryAdjustments is the inventoryAdjustments for the current year(9941
     * and 9942).
     */
    private java.lang.Double inventoryAdjustments;

    /** netIncomeAfterAdj is the netIncomeAfterAdjUSTMENTS amount (9944). */
    private java.lang.Double netIncomeAfterAdj;

    /**
     * businessUseOfHomeExpenses is the allowable portion of business-use-of-home
     * expenses (9934).
     */
    private java.lang.Double businessUseOfHomeExpenses;

    /** netFarmIncome is Net Farming Income after adjustments (9946). */
    private java.lang.Double netFarmIncome;

    /**
     * isCropDisaster is the productive capacity decreased due to disaster
     * circumstances indicator (from page 7 of the t1273). Allowable values are Y
     * - Yes, N - No.
     */
    private java.lang.Boolean isCropDisaster;

    /**
     * isLivestockDisaster is the productive capacity decreased due to disaster
     * circumstances indicator (from page 7 of the t1273). Allowable values are Y
     * - Yes, N - No.
     */
    private java.lang.Boolean isLivestockDisaster;

    /**
     * revisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     */
    private java.lang.Integer revisionCount;

    /** The Z04IncomeExpsDtl associated with this Z03StatementInfo. */
    private Z04IncomeExpsDtl[] z04IncomeExpsDtl;

    /** The Z05PartnerInfo associated with this Z03StatementInfo. */
    private Z05PartnerInfo[] z05PartnerInfo;

    /** The Z21ParticipantSuppl associated with this Z03StatementInfo. */
    private Z21ParticipantSuppl[] z21ParticipantSuppl;

    /** The Z22ProductionInsurance associated with this Z03StatementInfo. */
    private Z22ProductionInsurance[] z22ProductionInsurance;

    /** The Z23LivestockProdCpct associated with this Z03StatementInfo. */
    private Z23LivestockProdCpct[] z23LivestockProdCpct;

    /** The Z40PrtcpntRefSuplDtl associated with this Z03StatementInfo. */
    private Z40PrtcpntRefSuplDtl[] z40PrtcpntRefSuplDtl;

    /** Constructor. */
    public Z03StatementInfo() {

    }

    /**
     * @return Z04IncomeExpsDtl[]
     */
    public Z04IncomeExpsDtl[] getZ04IncomeExpsDtl() {
        return z04IncomeExpsDtl;
    }

    /**
     * @param newVal The new value for this property
     */
    public void setZ04IncomeExpsDtl(final Z04IncomeExpsDtl[] newVal) {
        z04IncomeExpsDtl = newVal;
    }

    /**
     * @return Z05PartnerInfo[]
     */
    public Z05PartnerInfo[] getZ05PartnerInfo() {
        return z05PartnerInfo;
    }

    /**
     * @param newVal The new value for this property
     */
    public void setZ05PartnerInfo(final Z05PartnerInfo[] newVal) {
        z05PartnerInfo = newVal;
    }

    /**
     * @return Z21ParticipantSuppl[]
     */
    public Z21ParticipantSuppl[] getZ21ParticipantSuppl() {
        return z21ParticipantSuppl;
    }

    /**
     * @param newVal The new value for this property
     */
    public void setZ21ParticipantSuppl(final Z21ParticipantSuppl[] newVal) {
        z21ParticipantSuppl = newVal;
    }

    /**
     * @return Z22ProductionInsurance[]
     */
    public Z22ProductionInsurance[] getZ22ProductionInsurance() {
        return z22ProductionInsurance;
    }

    /**
     * @param newVal The new value for this property
     */
    public void setZ22ProductionInsurance(final Z22ProductionInsurance[] newVal) {
        z22ProductionInsurance = newVal;
    }

    /**
     * @return Z23LivestockProdCpct[]
     */
    public Z23LivestockProdCpct[] getZ23LivestockProdCpct() {
        return z23LivestockProdCpct;
    }

    /**
     * @param newVal The new value for this property
     */
    public void setZ23LivestockProdCpct(final Z23LivestockProdCpct[] newVal) {
        z23LivestockProdCpct = newVal;
    }

    /**
     * @return Z40PrtcpntRefSuplDtl[]
     */
    public Z40PrtcpntRefSuplDtl[] getZ40PrtcpntRefSuplDtl() {
        return z40PrtcpntRefSuplDtl;
    }

    /**
     * @param newVal The new value for this property
     */
    public void setZ40PrtcpntRefSuplDtl(final Z40PrtcpntRefSuplDtl[] newVal) {
        z40PrtcpntRefSuplDtl = newVal;
    }

    /**
     * ParticipantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getParticipantPin() {
        return participantPin;
    }

    /**
     * ParticipantPin is the unique AgriStability/AgriInvest pin for this
     * prodcuer. Was previous CAIS Pin and NISA Pin.
     *
     * @param newVal The new value for this property
     */
    public void setParticipantPin(final java.lang.Integer newVal) {
        participantPin = newVal;
    }

    /**
     * ProgramYear is the stabilization year for this record.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getProgramYear() {
        return programYear;
    }

    /**
     * ProgramYear is the stabilization year for this record.
     *
     * @param newVal The new value for this property
     */
    public void setProgramYear(final java.lang.Integer newVal) {
        programYear = newVal;
    }

    /**
     * OperationNumber identifies each operation a participant reports for a given
     * stab year. Operations may have different statement numbers in different
     * program years.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getOperationNumber() {
        return operationNumber;
    }

    /**
     * OperationNumber identifies each operation a participant reports for a given
     * stab year. Operations may have different statement numbers in different
     * program years.
     *
     * @param newVal The new value for this property
     */
    public void setOperationNumber(final java.lang.Integer newVal) {
        operationNumber = newVal;
    }

    /**
     * PartnershipPin uniquely identifies the partnership. If both the partners in
     * an operation file applications, the same partnershipPin will show up under
     * both pins. partnershipPin will represent the same operation if/when they
     * are used in different program years.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getPartnershipPin() {
        return partnershipPin;
    }

    /**
     * PartnershipPin uniquely identifies the partnership. If both the partners in
     * an operation file applications, the same partnershipPin will show up under
     * both pins. partnershipPin will represent the same operation if/when they
     * are used in different program years.
     *
     * @param newVal The new value for this property
     */
    public void setPartnershipPin(final java.lang.Integer newVal) {
        partnershipPin = newVal;
    }

    /**
     * PartnershipName is the name of the partnership.
     *
     * @return String
     */
    public String getPartnershipName() {
        return partnershipName;
    }

    /**
     * PartnershipName is the name of the partnership.
     *
     * @param newVal The new value for this property
     */
    public void setPartnershipName(final String newVal) {
        partnershipName = newVal;
    }

    /**
     * PartnershipPercent is the percentage of the partnership. (100% will be
     * stored as 1.0).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getPartnershipPercent() {
        return partnershipPercent;
    }

    /**
     * PartnershipPercent is the percentage of the partnership. (100% will be
     * stored as 1.0).
     *
     * @param newVal The new value for this property
     */
    public void setPartnershipPercent(final java.lang.Double newVal) {
        partnershipPercent = newVal;
    }

    /**
     * FiscalYearStart is the Operation Fiscal year Start (yyyymmdd - may be
     * blank).
     *
     * @return String
     */
    public String getFiscalYearStart() {
        return fiscalYearStart;
    }

    /**
     * FiscalYearStart is the Operation Fiscal year Start (yyyymmdd - may be
     * blank).
     *
     * @param newVal The new value for this property
     */
    public void setFiscalYearStart(final String newVal) {
        fiscalYearStart = newVal;
    }

    /**
     * FiscalYearEnd is the Operation fiscalYearEnd (yyyymmdd - may be blank).
     *
     * @return String
     */
    public String getFiscalYearEnd() {
        return fiscalYearEnd;
    }

    /**
     * FiscalYearEnd is the Operation fiscalYearEnd (yyyymmdd - may be blank).
     *
     * @param newVal The new value for this property
     */
    public void setFiscalYearEnd(final String newVal) {
        fiscalYearEnd = newVal;
    }

    /**
     * AccountingCode is the Method of Accounting to CRA and AAFC.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getAccountingCode() {
        return accountingCode;
    }

    /**
     * AccountingCode is the Method of Accounting to CRA and AAFC.
     *
     * @param newVal The new value for this property
     */
    public void setAccountingCode(final java.lang.Integer newVal) {
        accountingCode = newVal;
    }

    /**
     * IsLandlord indicates if this Participant participated in a crop share as a
     * landlord. Allowable values are Y - Yes, N - No.
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isLandlord() {
        return isLandlord;
    }

    /**
     * IsLandlord indicates if this Participant participated in a crop share as a
     * landlord. Allowable values are Y - Yes, N - No.
     *
     * @param newVal The new value for this property
     */
    public void setIsLandlord(final java.lang.Boolean newVal) {
        isLandlord = newVal;
    }

    /**
     * IsCropShare indicates if this Participant participated in a crop share as a
     * tenant. Allowable values are Y - Yes, N - No.
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isCropShare() {
        return isCropShare;
    }

    /**
     * IsCropShare indicates if this Participant participated in a crop share as a
     * tenant. Allowable values are Y - Yes, N - No.
     *
     * @param newVal The new value for this property
     */
    public void setIsCropShare(final java.lang.Boolean newVal) {
        isCropShare = newVal;
    }

    /**
     * IsFeederMember indicates if this participant was a member of a feeder
     * association. Allowable values are Y - Yes, N - No.
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isFeederMember() {
        return isFeederMember;
    }

    /**
     * IsFeederMember indicates if this participant was a member of a feeder
     * association. Allowable values are Y - Yes, N - No.
     *
     * @param newVal The new value for this property
     */
    public void setIsFeederMember(final java.lang.Boolean newVal) {
        isFeederMember = newVal;
    }

    /**
     * GrossIncome is the grossIncome (9959).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getGrossIncome() {
        return grossIncome;
    }

    /**
     * GrossIncome is the grossIncome (9959).
     *
     * @param newVal The new value for this property
     */
    public void setGrossIncome(final java.lang.Double newVal) {
        grossIncome = newVal;
    }

    /**
     * Expenses is the Total Farming Expenses (9968).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getExpenses() {
        return expenses;
    }

    /**
     * Expenses is the Total Farming Expenses (9968).
     *
     * @param newVal The new value for this property
     */
    public void setExpenses(final java.lang.Double newVal) {
        expenses = newVal;
    }

    /**
     * NetIncomeBeforeAdj is the Net Income before Adjustments (9969).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getNetIncomeBeforeAdj() {
        return netIncomeBeforeAdj;
    }

    /**
     * NetIncomeBeforeAdj is the Net Income before Adjustments (9969).
     *
     * @param newVal The new value for this property
     */
    public void setNetIncomeBeforeAdj(final java.lang.Double newVal) {
        netIncomeBeforeAdj = newVal;
    }

    /**
     * OtherDeductions is the otherDeductions to the net income amount. (9940).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getOtherDeductions() {
        return otherDeductions;
    }

    /**
     * OtherDeductions is the otherDeductions to the net income amount. (9940).
     *
     * @param newVal The new value for this property
     */
    public void setOtherDeductions(final java.lang.Double newVal) {
        otherDeductions = newVal;
    }

    /**
     * InventoryAdjustments is the inventoryAdjustments for the current year(9941
     * and 9942).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getInventoryAdjustments() {
        return inventoryAdjustments;
    }

    /**
     * InventoryAdjustments is the inventoryAdjustments for the current year(9941
     * and 9942).
     *
     * @param newVal The new value for this property
     */
    public void setInventoryAdjustments(final java.lang.Double newVal) {
        inventoryAdjustments = newVal;
    }

    /**
     * NetIncomeAfterAdj is the netIncomeAfterAdjUSTMENTS amount (9944).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getNetIncomeAfterAdj() {
        return netIncomeAfterAdj;
    }

    /**
     * NetIncomeAfterAdj is the netIncomeAfterAdjUSTMENTS amount (9944).
     *
     * @param newVal The new value for this property
     */
    public void setNetIncomeAfterAdj(final java.lang.Double newVal) {
        netIncomeAfterAdj = newVal;
    }

    /**
     * BusinessUseOfHomeExpenses is the allowable portion of business-use-of-home
     * expenses (9934).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getBusinessUseOfHomeExpenses() {
        return businessUseOfHomeExpenses;
    }

    /**
     * BusinessUseOfHomeExpenses is the allowable portion of business-use-of-home
     * expenses (9934).
     *
     * @param newVal The new value for this property
     */
    public void setBusinessUseOfHomeExpenses(final java.lang.Double newVal) {
        businessUseOfHomeExpenses = newVal;
    }

    /**
     * NetFarmIncome is Net Farming Income after adjustments (9946).
     *
     * @return java.lang.Double
     */
    public java.lang.Double getNetFarmIncome() {
        return netFarmIncome;
    }

    /**
     * NetFarmIncome is Net Farming Income after adjustments (9946).
     *
     * @param newVal The new value for this property
     */
    public void setNetFarmIncome(final java.lang.Double newVal) {
        netFarmIncome = newVal;
    }

    /**
     * IsCropDisaster is the productive capacity decreased due to disaster
     * circumstances indicator (from page 7 of the t1273). Allowable values are Y
     * - Yes, N - No.
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isCropDisaster() {
        return isCropDisaster;
    }

    /**
     * IsCropDisaster is the productive capacity decreased due to disaster
     * circumstances indicator (from page 7 of the t1273). Allowable values are Y
     * - Yes, N - No.
     *
     * @param newVal The new value for this property
     */
    public void setIsCropDisaster(final java.lang.Boolean newVal) {
        isCropDisaster = newVal;
    }

    /**
     * IsLivestockDisaster is the productive capacity decreased due to disaster
     * circumstances indicator (from page 7 of the t1273). Allowable values are Y
     * - Yes, N - No.
     *
     * @return java.lang.Boolean
     */
    public java.lang.Boolean isLivestockDisaster() {
        return isLivestockDisaster;
    }

    /**
     * IsLivestockDisaster is the productive capacity decreased due to disaster
     * circumstances indicator (from page 7 of the t1273). Allowable values are Y
     * - Yes, N - No.
     *
     * @param newVal The new value for this property
     */
    public void setIsLivestockDisaster(final java.lang.Boolean newVal) {
        isLivestockDisaster = newVal;
    }

    /**
     * RevisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getRevisionCount() {
        return revisionCount;
    }

    /**
     * RevisionCount is a counter identifying the number of times this record as
     * been modified. Used in the web page access to determine if the record as
     * been modified since the data was first retrieved.
     *
     * @param newVal The new value for this property
     */
    public void setRevisionCount(final java.lang.Integer newVal) {
        revisionCount = newVal;
    }

}
