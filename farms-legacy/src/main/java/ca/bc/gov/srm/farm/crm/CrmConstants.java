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
package ca.bc.gov.srm.farm.crm;

/**
 * @author awilkinson
 */
public final class CrmConstants {

  public static final String HEADER_ENTITY_URL = "OData-EntityId";
  
  // Object Type Codes
  public static final Integer OBJECT_TYPE_CODE = 4212;
  
  public static final int TASK_STATE_CODE_OPEN = 0;
  public static final int TASK_STATE_CODE_COMPLETED = 1;
  public static final int TASK_STATE_CODE_CANCELLED = 2;
  
  public static final int TASK_STATUS_CODE_NOT_STARTED = 2;
  public static final int TASK_STATUS_CODE_IN_PROGRESS = 3;
  public static final int TASK_STATUS_CODE_COMPLETED = 5;
  
  // status code for vsi_noltasks and vsi_validationerrortasks tables
  public static final int STATUS_CODE_OPEN = 1;
  public static final int STATUS_CODE_COMPLETED = 2;
  public static final int STATUS_CODE_CANCELED = 3;
  public static final int STATUS_CODE_SCHEDULED = 4;
  
  public static final int ENROLMENT_STATUS_CODE_ENROLLED = 865520000;
  public static final int ENROLMENT_STATUS_CODE_LATE_ENROLLED = 865520011;
  public static final int ENROLMENT_STATUS_CODE_INITIALIZED = 865520004;
  public static final int ENROLMENT_STATUS_CODE_INELIGIBLE = 865520002;
  public static final int ENROLMENT_STATUS_CODE_TO_BE_REVIEWED = 865520009;
  
  
  // ------------------------ API Endpoints --------------------------------------------------------------- //
  public static final String ACCOUNT_ENDPOINT = "accounts";
  public static final String ANNOTATION_ENDPOINT = "annotations";
  public static final String BENEFIT_UPDATE_ENDPOINT = "vsi_transactionbenefits";
  public static final String BENEFIT_ENDPOINT = "vsi_benefits";
  public static final String ENROLMENT_UPDATE_ENDPOINT = "vsi_transactionenrolmentfees";
  public static final String TASK_ENDPOINT = "tasks";
  public static final String PROGRAM_YEAR_ENDPOINT = "vsi_programyears";
  public static final String ENROLMENT_ENDPOINT = "vsi_participantprogramyears";
  public static final String COVERAGE_NOTICE_TASK_ENDPOINT = "vsi_coveragenoticetasks";
  public static final String NOL_TASK_ENDPOINT = "vsi_noltasks";
  public static final String NPP_TASK_ENDPOINT = "vsi_newparticipanttasks";
  public static final String VALIDATION_ERROR_TASK_ENDPOINT = "vsi_validationerrortasks";
  public static final String QUEUE_ENDPOINT = "queues";
  public static final String QUEUEITEM_ENDPOINT = "queueitems";
  public static final String ROUTE_TO_ENDPOINT = "RouteTo";
  public static final String CORE_CONFIGURATION_ENDPOINT = "vsi_armsconfigurations";

  public static final String ACCOUNT_LOOKUP_QUERY_STRING = "?%24filter=vsi_pin%20eq%20%27{pin}%27";
  public static final String QUEUE_LOOKUP_QUERY_STRING = "?%24filter=name%20eq%20%27{name}%27";
  public static final String QUEUEITEM_LOOKUP_QUERY_STRING = "?%24filter=_objectid_value%20eq%20%27{taskId}%27";
  public static final String VALIDATION_ERROR_LOOKUP_QUERY_STRING =
      "?%24filter=contains(cr4dd_chefsurl,%27s={submissionGuid}%27)";
  public static final String QUERY_PARAM_TASK_ACTIVE_ONLY =
      "%20and%20statecode%20ne%20"  + TASK_STATE_CODE_COMPLETED +
      "%20and%20statecode%20ne%20" + TASK_STATE_CODE_CANCELLED;
  public static final String QUERY_PARAM_ORDER_BY_CREATED_ON_DESC = "&$orderby=createdon%20desc";
  public static final String BENEFIT_LOOKUP_QUERY_STRING = "?%24orderby=createdon%20desc&%24filter=_vsi_participantid_value%20eq%20%27{accountId}%27&%24expand=vsi_participantprogramyearid(%24select=vsi_programyearid;%24expand=vsi_programyearid(%24select=vsi_year))";
  public static final String PROGRAM_YEAR_QUERY_STRING = "?%24filter=vsi_year%20eq%20%27{year}%27";
  public static final String ENROLMENT_QUERY_STRING = "?%24filter=_vsi_programyearid_value%20eq%20%27{programyearid}%27%20and%20_vsi_participantid_value%20eq%20%27{accountid}%27";
  public static final String CORE_CONFIGURATION_QUERY_STRING = "?$select=vsi_triagepaymentthreshold&$orderby=modifiedon%20desc&$top=1";
  // ------------------------------------------------------------------------------------------------------- //
  
  
  // ------------------------ CRM App URLs ---------------------------------------------------------------- //
  public static final String APP_PATH_COVERAGE_NOTICE_TASK = "vsi_coveragenoticetask";
  public static final String APP_PATH_NOL_TASK = "vsi_noltask";
  public static final String APP_PATH_NPP_TASK = "vsi_newparticipanttask";
  public static final String APP_PATH_VALIDATION_ERROR_TASK = "vsi_validationerrortask";
  
  public static final String CRM_TASK_URL_FORMAT = "%s/main.aspx?appid=%s&pagetype=entityrecord&etn=%s&id=%s";
  // ------------------------------------------------------------------------------------------------------ //
  
  public static String getEnrolmentStatusDescription(int enrolmentStatusCode) {
    String description = null;
    
    switch (enrolmentStatusCode) {
    case ENROLMENT_STATUS_CODE_ENROLLED:
      description = "Enrolled";
      break;
    case ENROLMENT_STATUS_CODE_LATE_ENROLLED:
      description = "Late Enrolled";
      break;
    case ENROLMENT_STATUS_CODE_INITIALIZED:
      description = "Initialized";
      break;
    case ENROLMENT_STATUS_CODE_INELIGIBLE:
      description = "Ineligible";
      break;
    case ENROLMENT_STATUS_CODE_TO_BE_REVIEWED:
      description = "To Be Reviewed";
      break;
    }
    
    return description;
  }
  
  private CrmConstants() {
    // private constructor
  }
}
