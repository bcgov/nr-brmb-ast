/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * AgristabilityScenario refers to a unique instance of the associated data.
 * AgristabilityScenario will be enforced on Operation, Margin, Claim and all
 * Adjustments data according to specific business rules (i.e. tied to State
 * changes). Many instances of an AgristabilityScenario may exist for the above
 * listed entities.
 * 
 * @author awilkinson
 * @created Nov 12, 2010
 *
 */
public class Scenario extends ReferenceScenario {

  private static final long serialVersionUID = 8759879376362676300L;

  private Client client;
  
  private List<ReferenceScenario> referenceScenarios;

  private String finalVerificationNotes;
  
  private String interimVerificationNotes;
  
  private String adjustmentVerificationNotes;
  
  private Integer programYearRevisionCount;
  
  private Date programYearWhenCreatedTimestamp;
  
  private List<ScenarioStateAudit> scenarioStateAudits;
  
  private List<ScenarioMetaData> scenarioMetaDataList;
  
  private Enrolment enrolment;
  
  private EnwEnrolment enwEnrolment;
  
  private PreVerificationChecklist preVerificationChecklist;
  
  @JsonIgnore
  private ReasonabilityTestResults reasonabilityTestResults;

  /**
   * senarioStateCode is a unique code for the object senarioStateCode described
   * as a character code used to uniquely identify the state of the scenario.
   * Examples of codes and descriptions are : IP - IN PROGRESS, COMP - VERIFIED.
   */
  private String scenarioStateCode;

  /** Description for senarioStateCode. */
  private String scenarioStateCodeDescription;
  
  private String scenarioCategoryCode;

  private String scenarioCategoryCodeDescription;
  
  private String farmTypeCode;

  private String farmTypeCodeDescription;
  
  private Date craStatementAReceivedDate;
  
  private Date craSupplementalReceivedDate;
  
  private Date localSupplementalReceivedDate;
  
  private Date localStatementAReceivedDate;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  /**
   * assignedToUserGuid describes the name of the user to which the ProgramYear
   * is assigned. USER GUID describes each user or group in a directory using a
   * unique identifer called a GUID or Globally Unique Identifier. The value is
   * a 128 bit number but is displayed and passed around the netowork as a 32
   * character hex string. There is no guarantee that a user ID is unique.
   */
  private String assignedToUserGuid;

  /**
   * assignedToUserId is to which the ProgramYear has been assigned. USERID is
   * the user ID associated with the GUID that a user would use to log on with.
   * For this purpose only the username is stored in the record.
   */
  private String assignedToUserId;

  /**
   * 
   */
  private Date whenUpdatedTimestamp;
  
  private Date benefitDocCreatedDate;
  
  private List<ScenarioLog> scenarioLogs;
  
  private Boolean isInCombinedFarmInd;
  
  /** surrogate unique identifier for a COMBINED FARM */
  private Integer combinedFarmNumber;
  
  private List<CombinedFarmClient> combinedFarmClients;
  
  private CombinedFarm combinedFarm;
  
  private Integer chefsSubmissionId;
  
  private String crmTaskGuid;

  private String chefsSubmissionGuid;

  private String chefsFormTypeCode;
  
  private Integer verifierUserId;
  
  private String verifierAccountName;

  private String verifiedByEmail;
  
  private Map<String, BasePricePerUnit> inventoryBpus;
  private Map<String, BasePricePerUnit> structureGroupBpus;
  
  /** Used for Coverage Notice */
  private Map<String, BasePricePerUnit> earlierProgramYearInventoryBpus;
  private Map<String, BasePricePerUnit> earlierProgramYearStructureGroupBpus;
  
  /**
   */
  public Scenario() {
    setParentScenario(this);
  }

  /**
   * @return the agristabilityClient
   */
  public Client getClient() {
    return client;
  }

  /**
   * @param client the client to set
   */
  public void setClient(Client client) {
    if(client != null) {
      client.setScenario(this);
    }
    this.client = client;
  }

  public String getFinalVerificationNotes() {
    return finalVerificationNotes;
  }

  public void setFinalVerificationNotes(String value) {
    finalVerificationNotes = value;
  }

  public String getInterimVerificationNotes() {
    return interimVerificationNotes;
  }

  public void setInterimVerificationNotes(String programYearInterimComment) {
    this.interimVerificationNotes = programYearInterimComment;
  }

  public String getAdjustmentVerificationNotes() {
    return adjustmentVerificationNotes;
  }

  public void setAdjustmentVerificationNotes(String adjustmentVerificationNotes) {
    this.adjustmentVerificationNotes = adjustmentVerificationNotes;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  revisionCount  The new value for this property
   */
  public void setRevisionCount(final Integer revisionCount) {
    this.revisionCount = revisionCount;
  }


  /**
   * senarioStateCode is a unique code for the object senarioStateCode described
   * as a character code used to uniquely identify the state of the scenario.
   * Examples of codes and descriptions are : IP - IN PROGRESS, COMP - VERIFIED.
   *
   * @return  String
   */
  public String getScenarioStateCode() {
    return scenarioStateCode;
  }

  /**
   * senarioStateCode is a unique code for the object senarioStateCode described
   * as a character code used to uniquely identify the state of the scenario.
   * Examples of codes and descriptions are : IP - IN PROGRESS, COMP - VERIFIED.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioStateCode(final String newVal) {
    scenarioStateCode = newVal;
  }

  /**
   * Description for senarioStateCode.
   *
   * @return  String
   */
  public String getScenarioStateCodeDescription() {
    return scenarioStateCodeDescription;
  }

  /**
   * Description for senarioStateCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioStateCodeDescription(final String newVal) {
    scenarioStateCodeDescription = newVal;
  }
  
  /**
   * @return  String
   */
  public String getScenarioCategoryCode() {
    return scenarioCategoryCode;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setScenarioCategoryCode(final String newVal) {
    scenarioCategoryCode = newVal;
  }

  /**
   * @return  String
   */
  public String getScenarioCategoryCodeDescription() {
    return scenarioCategoryCodeDescription;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setScenarioCategoryCodeDescription(final String newVal) {
    scenarioCategoryCodeDescription = newVal;
  }

  public Enrolment getEnrolment() {
    return enrolment;
  }

  public void setEnrolment(Enrolment enrolment) {
    if(enrolment != null) {
      enrolment.setScenario(this);
    }
    this.enrolment = enrolment;
  }

  public EnwEnrolment getEnwEnrolment() {
    return enwEnrolment;
  }

  public void setEnwEnrolment(EnwEnrolment enwEnrolment) {
    if(enwEnrolment != null) {
      enwEnrolment.setScenario(this);
    }
    this.enwEnrolment = enwEnrolment;
  }

  /**
   * @return the referenceScenarios
   */
  public List<ReferenceScenario> getReferenceScenarios() {
    return referenceScenarios;
  }

  /**
   * @param referenceScenarios the referenceScenarios to set
   */
  public void setReferenceScenarios(List<ReferenceScenario> referenceScenarios) {
    if(referenceScenarios != null) {
      for(ReferenceScenario cur : referenceScenarios) {
        cur.setParentScenario(this);
      }
    }
    this.referenceScenarios = referenceScenarios;
  }

  /**
   * @return the scenarioStateAudits
   */
  public List<ScenarioStateAudit> getScenarioStateAudits() {
    return scenarioStateAudits;
  }

  /**
   * @param scenarioStateAudits the scenarioStateAudits to set
   */
  public void setScenarioStateAudits(List<ScenarioStateAudit> scenarioStateAudits) {
    if(scenarioStateAudits != null) {
      for(ScenarioStateAudit cur : scenarioStateAudits) {
        cur.setScenario(this);
      }
    }
    this.scenarioStateAudits = scenarioStateAudits;
  }

  /**
   * AssignedToUserGuid describes the name of the user to which the ProgramYear
   * is assigned. USER GUID describes each user or group in a directory using a
   * unique identifer called a GUID or Globally Unique Identifier. The value is
   * a 128 bit number but is displayed and passed around the netowork as a 32
   * character hex string. There is no guarantee that a user ID is unique.
   *
   * @return  String
   */
  public String getAssignedToUserGuid() {
    return assignedToUserGuid;
  }

  /**
   * AssignedToUserGuid describes the name of the user to which the ProgramYear
   * is assigned. USER GUID describes each user or group in a directory using a
   * unique identifer called a GUID or Globally Unique Identifier. The value is
   * a 128 bit number but is displayed and passed around the netowork as a 32
   * character hex string. There is no guarantee that a user ID is unique.
   *
   * @param  newVal  The new value for this property
   */
  public void setAssignedToUserGuid(final String newVal) {
    assignedToUserGuid = newVal;
  }

  /**
   * AssignedToUserid is to which the ProgramYear has been assigned. USERID is
   * the user ID associated with the GUID that a user would use to log on with.
   * For this purpose only the username is stored in the record.
   *
   * @return  String
   */
  public String getAssignedToUserId() {
    return assignedToUserId;
  }

  /**
   * AssignedToUserid is to which the ProgramYear has been assigned. USERID is
   * the user ID associated with the GUID that a user would use to log on with.
   * For this purpose only the username is stored in the record.
   *
   * @param  newVal  The new value for this property
   */
  public void setAssignedToUserId(final String newVal) {
    assignedToUserId = newVal;
  }

  /**
   * Gets whenUpdatedTimestamp
   *
   * @return the whenUpdatedTimestamp
   */
  public Date getWhenUpdatedTimestamp() {
    return whenUpdatedTimestamp;
  }

  /**
   * Sets whenUpdatedTimestamp
   *
   * @param whenUpdatedTimestamp the whenUpdatedTimestamp to set
   */
  public void setWhenUpdatedTimestamp(Date whenUpdatedTimestamp) {
    this.whenUpdatedTimestamp = whenUpdatedTimestamp;
  }

  public List<ScenarioMetaData> getScenarioMetaDataList() {
    return scenarioMetaDataList;
  }

  public void setScenarioMetaDataList(List<ScenarioMetaData> scenarioMetaDataList) {
    if(scenarioMetaDataList != null) {
      for(ScenarioMetaData cur : scenarioMetaDataList) {
        cur.setScenario(this);
      }
    }
    this.scenarioMetaDataList = scenarioMetaDataList;
  }

  /**
   * @return the cobGenerationDate
   */
  public Date getBenefitDocCreatedDate() {
    return benefitDocCreatedDate;
  }

  /**
   * @param cobGenerationDate the cobGenerationDate to set the value to
   */
  public void setBenefitDocCreatedDate(Date benefitDocCreatedDate) {
    this.benefitDocCreatedDate = benefitDocCreatedDate;
  }
  
  /**
   * Gets programYearRevisionCount
   *
   * @return the programYearRevisionCount
   */
  public Integer getProgramYearRevisionCount() {
    return programYearRevisionCount;
  }

  /**
   * Sets programYearRevisionCount
   *
   * @param pProgramYearRevisionCount the programYearRevisionCount to set
   */
  public void setProgramYearRevisionCount(Integer pProgramYearRevisionCount) {
    programYearRevisionCount = pProgramYearRevisionCount;
  }

  /**
	 * @return the scenarioLogs
	 */
	public List<ScenarioLog> getScenarioLogs() {
		return scenarioLogs;
	}

	/**
   * Gets farmTypeCode
   *
   * @return the farmTypeCode
   */
  public String getFarmTypeCode() {
    return farmTypeCode;
  }

  /**
   * Sets farmTypeCode
   *
   * @param pFarmTypeCode the farmTypeCode to set
   */
  public void setFarmTypeCode(String pFarmTypeCode) {
    farmTypeCode = pFarmTypeCode;
  }

  /**
   * Gets farmTypeDetailed
   * 
   * @return the farmTypeDetailed
   */
  public List<String> getFarmTypeDetailedCodes() {
    return ScenarioUtils.getFarmTypeDetailedCodes(this);
  }

  public List<String> getFarmTypeDetailedDescriptions() {
    return ScenarioUtils.getFarmTypeDetailedDescriptions(this);
  }

  public List<ProductiveUnitCapacity> getFarmTypeDetailedProductiveCapacities() {
    return ScenarioUtils.getFarmTypeDetailedProductiveCapacities(this);
  }

  /**
   * Gets farmTypeCodeDescription
   *
   * @return the farmTypeCodeDescription
   */
  public String getFarmTypeCodeDescription() {
    return farmTypeCodeDescription;
  }

  /**
   * Sets farmTypeCodeDescription
   *
   * @param pFarmTypeCodeDescription the farmTypeCodeDescription to set
   */
  public void setFarmTypeCodeDescription(String pFarmTypeCodeDescription) {
    farmTypeCodeDescription = pFarmTypeCodeDescription;
  }

  public Boolean getIsInCombinedFarmInd() {
    return isInCombinedFarmInd;
  }

  public void setIsInCombinedFarmInd(Boolean isInCombinedFarmInd) {
    this.isInCombinedFarmInd = isInCombinedFarmInd;
  }

  public Integer getCombinedFarmNumber() {
      return combinedFarmNumber;
  }
  
  public void setCombinedFarmNumber(Integer combinedFarmNumber) {
      this.combinedFarmNumber = combinedFarmNumber;
  }
  
  public List<CombinedFarmClient> getCombinedFarmClients() {
    return combinedFarmClients;
  }

  public void setCombinedFarmClients(List<CombinedFarmClient> combinedFarmClients) {
    this.combinedFarmClients = combinedFarmClients;
  }

  /**
	 * @param scenarioLogs the scenarioLogs to set
	 */
	public void setScenarioLogs(List<ScenarioLog> scenarioLogs) {
    if(scenarioLogs != null) {
      for(ScenarioLog cur : scenarioLogs) {
        cur.setScenario(this);
      }
    }
		this.scenarioLogs = scenarioLogs;
	}

	/**
   * @return the combinedFarm
   */
  public CombinedFarm getCombinedFarm() {
    return combinedFarm;
  }

  /**
   * @param combinedFarm the combinedFarm to set
   */
  public void setCombinedFarm(CombinedFarm combinedFarm) {
    this.combinedFarm = combinedFarm;
  }

  /**
   * @return the programYearWhenCreatedTimestamp
   */
  public Date getProgramYearWhenCreatedTimestamp() {
    return programYearWhenCreatedTimestamp;
  }

  /**
   * @param programYearWhenCreatedTimestamp the programYearWhenCreatedTimestamp to set
   */
  public void setProgramYearWhenCreatedTimestamp(Date programYearWhenCreatedTimestamp) {
    this.programYearWhenCreatedTimestamp = programYearWhenCreatedTimestamp;
  }

  public Date getCraStatementAReceivedDate() {
    return craStatementAReceivedDate;
  }

  public void setCraStatementAReceivedDate(Date craStatementAReceivedDate) {
    this.craStatementAReceivedDate = craStatementAReceivedDate;
  }

  /**
   * @return the craSupplementalReceivedDate
   */
  public Date getCraSupplementalReceivedDate() {
    return craSupplementalReceivedDate;
  }

  /**
   * @param craSupplementalReceivedDate the craSupplementalReceivedDate to set
   */
  public void setCraSupplementalReceivedDate(Date craSupplementalReceivedDate) {
    this.craSupplementalReceivedDate = craSupplementalReceivedDate;
  }

  public Date getLocalSupplementalReceivedDate() {
    return localSupplementalReceivedDate;
  }

  public void setLocalSupplementalReceivedDate(Date localSupplementalReceivedDate) {
    this.localSupplementalReceivedDate = localSupplementalReceivedDate;
  }

  public ReasonabilityTestResults getReasonabilityTestResults() {
    return reasonabilityTestResults;
  }

  public void setReasonabilityTestResults(ReasonabilityTestResults reasonabilityTestResults) {
    if(reasonabilityTestResults != null) {
      reasonabilityTestResults.setScenario(this);
    }
    this.reasonabilityTestResults = reasonabilityTestResults;
  }

  public PreVerificationChecklist getPreVerificationChecklist() {
    return preVerificationChecklist;
  }

  public void setPreVerificationChecklist(PreVerificationChecklist preVerificationChecklist) {
    this.preVerificationChecklist = preVerificationChecklist;
  }

  public Integer getChefsSubmissionId() {
    return chefsSubmissionId;
  }

  public void setChefsSubmissionId(Integer chefsSubmissionId) {
    this.chefsSubmissionId = chefsSubmissionId;
  }

  public String getCrmTaskGuid() {
    return crmTaskGuid;
  }

  public void setCrmTaskGuid(String crmTaskGuid) {
    this.crmTaskGuid = crmTaskGuid;
  }

  public String getChefsSubmissionGuid() {
		return chefsSubmissionGuid;
	}

	public void setChefsSubmissionGuid(String chefsSubmissionGuid) {
		this.chefsSubmissionGuid = chefsSubmissionGuid;
	}

  public String getChefsFormTypeCode() {
    return chefsFormTypeCode;
  }

  public void setChefsFormTypeCode(String chefsFormTypeCode) {
    this.chefsFormTypeCode = chefsFormTypeCode;
  }

  public Date getLocalStatementAReceivedDate() {
    return localStatementAReceivedDate;
  }

  public void setLocalStatementAReceivedDate(Date localStatementAReceivedDate) {
    this.localStatementAReceivedDate = localStatementAReceivedDate;
  }
  
  public Integer getVerifierUserId() {
    return verifierUserId;
  }

  public void setVerifierUserId(Integer verifierUserId) {
    this.verifierUserId = verifierUserId;
  }
  
  public Map<String, BasePricePerUnit> getInventoryBpus() {
    return inventoryBpus;
  }

  public void setInventoryBpus(Map<String, BasePricePerUnit> inventoryBpus) {
    this.inventoryBpus = inventoryBpus;
  }

  public Map<String, BasePricePerUnit> getStructureGroupBpus() {
    return structureGroupBpus;
  }

  public void setStructureGroupBpus(Map<String, BasePricePerUnit> structureGroupBpus) {
    this.structureGroupBpus = structureGroupBpus;
  }

  public Map<String, BasePricePerUnit> getEarlierProgramYearInventoryBpus() {
    return earlierProgramYearInventoryBpus;
  }

  public void setEarlierProgramYearInventoryBpus(Map<String, BasePricePerUnit> earlierProgramYearInventoryBpus) {
    this.earlierProgramYearInventoryBpus = earlierProgramYearInventoryBpus;
  }

  public Map<String, BasePricePerUnit> getEarlierProgramYearStructureGroupBpus() {
    return earlierProgramYearStructureGroupBpus;
  }

  public void setEarlierProgramYearStructureGroupBpus(Map<String, BasePricePerUnit> earlierProgramYearStructureGroupBpus) {
    this.earlierProgramYearStructureGroupBpus = earlierProgramYearStructureGroupBpus;
  }

  /**
   * @return true if this scenario is part of a combined farm
   */
  @JsonIgnore
  public boolean isInCombinedFarm() {
    return isInCombinedFarmInd.booleanValue();
  }
  
  /**
   * @return true if the Cob has been generated
   */
  @JsonIgnore
  public boolean isHasBenefitDocument() {
    return benefitDocCreatedDate != null;
  }
  
  /**
   * @return true if the scenario's state is Verified
   */
  @JsonIgnore
  public boolean isVerified() {
    return ScenarioStateCodes.VERIFIED.equals(scenarioStateCode);
  }
  
  /**
   * @return true if the scenario's state is Amended
   */
  @JsonIgnore
  public boolean isAmended() {
    return ScenarioStateCodes.AMENDED.equals(scenarioStateCode);
  }
  
  /**
   * @return true if the scenario's state is Completed
   */
  @JsonIgnore
  public boolean isCompleted() {
    return ScenarioStateCodes.COMPLETED.equals(scenarioStateCode);
  }
  
  /**
   * @return true if the scenario's state is In Progress
   */
  @JsonIgnore
  public boolean isInProgress() {
    return ScenarioStateCodes.IN_PROGRESS.equals(scenarioStateCode);
  }
  
  /**
   * @return true if the scenario's category is Interim
   */
  @JsonIgnore
  public boolean isInterim() {
    return ScenarioCategoryCodes.INTERIM.equals(scenarioCategoryCode);
  }
  
  /**
   * @return true if the scenario's category is Coverage Notice
   */
  @JsonIgnore
  public boolean isCoverageNotice() {
    return ScenarioCategoryCodes.COVERAGE_NOTICE.equals(scenarioCategoryCode);
  }
  
  /**
   * @return true if the scenario's category is Verified
   */
  @JsonIgnore
  public boolean isPreVerification() {
    return ScenarioCategoryCodes.PRE_VERIFICATION.equals(scenarioCategoryCode);
  }
  
  /**
   * @return true if this is a Late Participant.
   */
  @JsonIgnore
  public boolean isLateParticipant() {
    return getFarmingYear().getIsLateParticipant().booleanValue();
  }
  
  /**
   * @return  how many years of data there is
   */
  @JsonIgnore
  public int getNumYears() {
    return getAllScenarios().size();
  }

  /**
   * @return  the years that we are supposed to have reference data for.
   */
  @JsonIgnore
  public List<Integer> getRequiredReferenceYears() {
    final int maxNumRefYears = 5;
    List<Integer> years = new ArrayList<>(maxNumRefYears);
    int year = getYear().intValue() - maxNumRefYears;
    
    for (int ii = 0; ii < maxNumRefYears; ii++) {
      years.add(new Integer(year));
      year++;
    }

    return years;
  }

  /**
   * @return The assignedToUserId with IDIR\ stripped off the start.
   */
  @JsonIgnore
  public String getAssignedToUserIdDisplay() {
    final int idirLength = 5;
    String result;
    if(assignedToUserId == null) {
      result = null;
    } else {
      if(assignedToUserId.toUpperCase().startsWith("IDIR\\")
          && assignedToUserId.length() > idirLength) {
        result = assignedToUserId.substring(idirLength);
      } else {
        result = assignedToUserId;
      }
    }
    return result;
  }


  /**
   * All the reference scenarios and the current year scenario.
   * @return List<ReferenceScenario>
   */
  @JsonIgnore
  public List<ReferenceScenario> getAllScenarios() {
    List<ReferenceScenario> allScenarios = new ArrayList<>();
    if(getReferenceScenarios() != null) {
      allScenarios.addAll(getReferenceScenarios());
    }
    allScenarios.add(this);
    return allScenarios;
  }
  
  public ReferenceScenario getReferenceScenarioByYear(Integer year) {
    ReferenceScenario rs = null;
    
    if(year != null) {
      for(ReferenceScenario cur : getAllScenarios()) {
        if(year.equals(cur.getYear())) {
          rs = cur;
        }
      }
    }
    
    return rs;
  }

  public List<ReferenceScenario> getReferenceScenariosByYear(Integer year) {
    List<ReferenceScenario> result = new ArrayList<>();
    if(combinedFarm != null) {
      result.addAll(combinedFarm.getReferenceScenariosByYear(year));
    } else {
      ReferenceScenario rs = getReferenceScenarioByYear(year);
      if(rs != null) {
        result.add(rs);
      }
    }
    return result;
  }
  
  @JsonIgnore
  public List<Scenario> getProgramYearScenarios() {
    List<Scenario> result = new ArrayList<>();
    if(isInCombinedFarm()) {
      result.addAll(combinedFarm.getScenarios());
    } else {
      result.add(this);
    }
    return result;
  }
  
  /**
   * @return true if this is a scenario of type USER
   */
  @JsonIgnore
  public boolean isUserScenario() {
    return ScenarioTypeCodes.USER.equals(getScenarioTypeCode());
  }
  
  /**
   * @return true if this is a scenario of type FIFO
   */
  @JsonIgnore
  public boolean isFifoScenario() {
    return ScenarioTypeCodes.FIFO.equals(getScenarioTypeCode());
  }
  
  /**
   * @return true if this is a scenario of category UNKNOWN
   */
  @JsonIgnore
  public boolean isUnknownCategory() {
    return ScenarioCategoryCodes.UNKNOWN.equals(getScenarioCategoryCode());
  }

  /**
   * @return true if this is a scenario of category ENROLMENT_NOTICE_WORKFLOW (ENW)
   */
  @JsonIgnore
  public boolean isEnrolmentNoticeWorkflow() {
    return ScenarioCategoryCodes.ENROLMENT_NOTICE_WORKFLOW.equals(getScenarioCategoryCode());
  }

  /**
   * @return the margin totals for all years
   */
  @JsonIgnore
  public Map<Integer, MarginTotal> getYearMargins() {
    Map<Integer, MarginTotal> yearMargins;
    
    if(isInCombinedFarm()) {
      yearMargins = combinedFarm.getYearMargins();
    } else {
      yearMargins = new HashMap<>();
      for(ReferenceScenario rs : getAllScenarios()) {
        Integer year = rs.getYear();
        MarginTotal mt = rs.getFarmingYear().getMarginTotal();
        yearMargins.put(year, mt);
      }
    }
    
    return yearMargins;
  }
  
  /**
   * @return the margin totals for the reference years
   */
  @JsonIgnore
  public Map<Integer, MarginTotal> getReferenceYearMargins() {
    Map<Integer, MarginTotal> refYearMargins = new HashMap<>();
    
    refYearMargins.putAll(getYearMargins());
    refYearMargins.remove(getYear());
    
    return refYearMargins;
  }

  /**
   * @return Benefit
   */
  @JsonIgnore
  public Benefit getBenefit() {
    Benefit benefit = null;
    if(combinedFarm != null) {
      benefit = combinedFarm.getBenefit();
    } else {
      FarmingYear farmingYear = getFarmingYear();
      if(farmingYear != null) {
        benefit = farmingYear.getBenefit();
      }
    }
    return benefit;
  }
  
  
  @JsonIgnore
  public boolean isHasBeenVerified() {
    return hasBeenOneOfStates(
        ScenarioStateCodes.VERIFIED,
        ScenarioStateCodes.AMENDED);
  }
  
  @JsonIgnore
  public boolean isHasBeenEnrolmentNoticeComplete() {
    return hasBeenOneOfStates(ScenarioStateCodes.ENROLMENT_NOTICE_COMPLETE);
  }
  
  @JsonIgnore
  public boolean isHasBeenPreVerified() {
    return hasBeenOneOfStates(ScenarioStateCodes.PRE_VERIFIED);
  }
  
  
  @JsonIgnore
  public boolean isProgramYearHasVerifiedFinal() {
    return ScenarioUtils.programYearHasVerifiedFinal(this);
  }
  
  
  @JsonIgnore
  public boolean isHasMultistageCommodity() {
    return ScenarioUtils.hasMultistageCommodity(this);
  }

  public boolean hasBeenOneOfStates(String... states) {
    List<String> stateList = Arrays.asList(states);
    boolean result = false;
    
    for(ScenarioStateAudit stateAudit : scenarioStateAudits) {
      String curStateCode = stateAudit.getScenarioStateCode();
      
      if(stateList.contains(curStateCode)) {
        result = true;
        break;
      }
    }
    
    return result;
  }
  
  public boolean stateIsOneOf(String... states) {
    List<String> stateList = Arrays.asList(states);
    boolean result = stateList.contains(getScenarioStateCode());
    return result;
  }
  
  public boolean categoryIsOneOf(String... categories) {
    List<String> categoryList = Arrays.asList(categories);
    boolean result = categoryList.contains(getScenarioCategoryCode());
    return result;
  }
  
  public boolean typeIsOneOf(String... types) {
    List<String> typeList = Arrays.asList(types);
    boolean result = typeList.contains(getScenarioTypeCode());
    return result;
  }
  
  
  @JsonIgnore
  public boolean isRealBenefit() {
    return ScenarioUtils.categoryIsRealBenefit(scenarioCategoryCode);
  }
  
  @JsonIgnore
  public boolean isBaseData() {
    return ScenarioTypeCodes.isBaseData(getScenarioTypeCode());
  }
  
  /**
   * @return boolean true if the benefit has been successfully calculated.
   *                 If the scenario is In Progress, check validation.
   * @throws Exception On Exception
   */
  @JsonIgnore
  public boolean isBenefitSuccessfullyCalculated() throws Exception {
    boolean result = false;
    boolean inProgress = ScenarioStateCodes.IN_PROGRESS.equals(scenarioStateCode);
    Benefit benefit = getFarmingYear().getBenefit();
    boolean hasTotalBenefit = benefit != null
        && benefit.getTotalBenefit() != null;

    if(inProgress) {
      if(hasTotalBenefit) {
        boolean missingInterimBenefitPercent = ScenarioCategoryCodes.INTERIM.equals(scenarioCategoryCode)
            && (benefit == null || benefit.getInterimBenefitPercent() == null);
        if(!missingInterimBenefitPercent) {
          BenefitService benefitService = ServiceFactory.getBenefitService();
          
          String userId = CurrentUser.getUser().getUserId();
          result = benefitService.getAllValidationErrors(this, userId).isEmpty();
        }
      }
    } else {
      result = hasTotalBenefit;
    }
    return result;
  }


  @JsonIgnore
  public boolean isEnwEnrolmentCalculated() throws Exception {
    boolean result = getEnwEnrolment() != null && getEnwEnrolment().getEnwEnrolmentId() != null;
    return result;
  }


  public boolean hasDataForYear(Integer refYear) {
    List<ReferenceScenario> referenceScenariosByYear = getReferenceScenariosByYear(refYear);
    boolean result = !referenceScenariosByYear.isEmpty();
    return result;
  }

  @JsonIgnore
  public List<Scenario> getParentScenarios() {

    List<Scenario> scenarios;
    if(isInCombinedFarm()) {
      scenarios = getCombinedFarm().getScenarios();
    } else {
      scenarios = new ArrayList<>();
      scenarios.add(this);
    }
    return scenarios;
  }

  @JsonIgnore
  public boolean isProductiveUnitsRollupEnabled() {
    return CalculatorConfig.isProdutiveUnitsRollupEnabled(getYear());
  }
  

  public String getVerifiedByEmail() {
    return verifiedByEmail;
  }

  public void setVerifiedByEmail(String verifiedByEmail) {
    this.verifiedByEmail = verifiedByEmail;
  }

  public String getVerifierAccountName() {
    return verifierAccountName;
  }

  public void setVerifierAccountName(String verifierAccountName) {
    this.verifierAccountName = verifierAccountName;
  }
  
  @JsonIgnore
  public boolean isNegativeMarginCalculationEnabled() {
    
    boolean userScenario = isUserScenario();
    boolean inProgress = isInProgress();
    boolean enabledForProgramYear = CalculatorConfig.isNegativeMarginCalculationEnabled(getYear());
    boolean interim = isInterim();
    Benefit benefit = getBenefit();
    
    boolean result = false;
    
    if(enabledForProgramYear
        && userScenario
        && ! interim
        && benefit != null) {
    
      Boolean piDeemedBenefitManualCalc = benefit.getProdInsurDeemedBenefitManuallyCalculated();
      boolean piManuallyCalculated = 
          ! inProgress
          && piDeemedBenefitManualCalc != null
          && piDeemedBenefitManualCalc;
    
      Double negativeMarginBenefit = benefit.getNegativeMarginBenefit();
      result =
          ! piManuallyCalculated
          && negativeMarginBenefit != null
          && negativeMarginBenefit > 0;
    }
      
    return result;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    String commentsOutput = null;
    if(finalVerificationNotes != null) {
      commentsOutput = "not null";
    }
    
    Integer referenceScenariosCount = null;
    if(referenceScenarios != null) {
      referenceScenariosCount = new Integer(referenceScenarios.size());
    }
    
    Integer combinedFarmClientsCount = null;
    if(combinedFarmClients != null) {
      combinedFarmClientsCount = new Integer(combinedFarmClients.size());
    }

    return "AgristabilityScenario"+"\n"+
    "\t client : "+client+"\n"+
    "\t scenarioStateCode : "+scenarioStateCode+"\n"+
    "\t scenarioStateCodeDescription : "+scenarioStateCodeDescription+"\n"+
    "\t scenarioCategoryCode : "+scenarioCategoryCode+"\n"+
    "\t scenarioCategoryCodeDescription : "+scenarioCategoryCodeDescription+"\n"+
    "\t assignedToUserGuid : "+assignedToUserGuid+"\n"+
    "\t assignedToUserId : "+assignedToUserId+"\n"+
    "\t farmTypeCode : "+farmTypeCode+"\n"+
    "\t farmTypeCodeDescription : "+farmTypeCodeDescription+"\n"+
    "\t craStatementAReceivedDate : "+craStatementAReceivedDate+"\n"+
    "\t craSupplementalReceivedDate : "+craSupplementalReceivedDate+"\n"+
    "\t localSupplementalReceivedDate : "+localSupplementalReceivedDate+"\n"+
    "\t localStatementAReceivedDate : "+localStatementAReceivedDate+"\n"+
    "\t programYearRevisionCount : "+programYearRevisionCount+"\n"+
    "\t programYearWhenCreatedTimestamp : "+programYearWhenCreatedTimestamp+"\n"+
    "\t referenceScenarios : "+referenceScenariosCount+"\n"+
    "\t whenUpdatedTimestamp : "+whenUpdatedTimestamp+"\n"+
    "\t comments : "+commentsOutput+"\n"+
    "\t chefsSubmissionId : "+chefsSubmissionId+"\n"+
    "\t crmTaskGuid : "+crmTaskGuid+"\n"+
    "\t combinedFarmNumber : "+combinedFarmNumber+"\n"+
    "\t combinedFarmClients : "+combinedFarmClientsCount+"\n"+
    "\t preVerificationChecklist : "+preVerificationChecklist+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t verifiyUserId : "+verifierUserId+"\n"+
    "\t verifiedByEmail : "+verifiedByEmail+"\n"+
    "\t verifierAccountName : "+verifierAccountName+"\n"+
    "[ : "+super.toString()+"]\n";
  }

}
