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
  
  public static final String ACCOUNT_ENDPOINT = "accounts";
  public static final String ANNOTATION_ENDPOINT = "annotations";
  public static final String BENEFIT_UPDATE_ENDPOINT = "vsi_transactionbenefits";
  public static final String BENEFIT_ENDPOINT = "vsi_benefits";
  public static final String ENROLMENT_UPDATE_ENDPOINT = "vsi_transactionenrolmentfees";
  public static final String TASK_ENDPOINT = "tasks";
  public static final String PROGRAM_YEAR_ENDPOINT = "vsi_programyears";
  public static final String ENROLMENT_ENDPOINT = "vsi_participantprogramyears";
  
  public static final String COVERAGE_TASK_ENDPOINT = "vsi_coveragenoticetasks";
  public static final String NOL_TASK_ENDPOINT = "vsi_noltasks";
  public static final String NPP_TASK_ENDPOINT = "vsi_newparticipanttasks";
  public static final String VALIDATION_ERROR_ENDPOINT = "vsi_validationerrortasks";
  
  public static final String QUEUE_ENDPOINT = "queues";
  public static final String QUEUEITEM_ENDPOINT = "queueitems";

  public static final String ROUTE_TO_ENDPOINT = "RouteTo";
  
  public static final int TASK_STATE_CODE_OPEN = 0;
  public static final int TASK_STATE_CODE_COMPLETED = 1;
  
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
  
  private CrmConstants() {
    // private constructor
  }
}
