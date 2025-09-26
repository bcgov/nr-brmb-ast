/**
 *
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

import static ca.bc.gov.srm.farm.crm.CrmConstants.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.exception.ServiceException;


public class CrmConfigurationUtil {

  public static final String ACCOUNT_LOOKUP_QUERY_STRING = "?%24filter=vsi_pin%20eq%20%27{pin}%27";
  public static final String QUEUE_LOOKUP_QUERY_STRING = "?%24filter=name%20eq%20%27{name}%27";
  public static final String QUEUEITEM_LOOKUP_QUERY_STRING = "?%24filter=_objectid_value%20eq%20%27{taskId}%27";
  public static final String VALIDATION_ERROR_LOOKUP_QUERY_STRING = "?%24filter=contains(cr4dd_chefsurl,%27s={submissionId}%27)";
  public static final String BENEFIT_LOOKUP_QUERY_STRING = "?%24orderby=createdon%20desc&%24filter=_vsi_participantid_value%20eq%20%27{accountId}%27&%24expand=vsi_participantprogramyearid(%24select=vsi_programyearid;%24expand=vsi_programyearid(%24select=vsi_year))";
  public static final String PROGRAM_YEAR_QUERY_STRING = "?%24filter=vsi_year%20eq%20%27{year}%27";
  public static final String ENROLMENT_QUERY_STRING = "?%24filter=_vsi_programyearid_value%20eq%20%27{programyearid}%27%20and%20_vsi_participantid_value%20eq%20%27{accountid}%27";

  private static CrmConfigurationUtil instance = null;
  
  private static ConfigurationUtility configUtil = ConfigurationUtility.getInstance();

  public static CrmConfigurationUtil getInstance() {

    if (instance == null) {
      instance = new CrmConfigurationUtil();
    }

    return instance;
  }

  public String getBenefitUpdateUrl() {
    return getUrl(BENEFIT_UPDATE_ENDPOINT);
  }

  public String getEnrolmentUpdateUrl() {
    return getUrl(ENROLMENT_UPDATE_ENDPOINT);
  }

  public String getTaskUrl() {
    return getUrl(TASK_ENDPOINT);
  }
  
  public String getValidationErrorUrl() {
    return getUrl(VALIDATION_ERROR_ENDPOINT);
  }

  public String getCoverageTaskUrl() {
    return getUrl(COVERAGE_TASK_ENDPOINT);
  }

  public String getNolTaskUrl() {
  	return getUrl(NOL_TASK_ENDPOINT);
  }
  
  public String getNppTaskUrl() {
  	return getUrl(NPP_TASK_ENDPOINT);
  }

  public String getAccountUrl() {
    return getUrl(ACCOUNT_ENDPOINT);
  }
  
  public String getAnnotationUrl() {
    return getUrl(ANNOTATION_ENDPOINT);
  }

  public String getQueueUrl() {
    return getUrl(QUEUE_ENDPOINT);
  }

  public String getQueueItemUrl() {
    return getUrl(QUEUEITEM_ENDPOINT);
  }
  
  public String getRouteToUrl() {
    return getUrl(ROUTE_TO_ENDPOINT);
  }

  public String getAccountLookupUrl(Integer participantPin) {
    String endpointUrl = getUrl(ACCOUNT_ENDPOINT);
    
    String result = endpointUrl + ACCOUNT_LOOKUP_QUERY_STRING.replace("{pin}", participantPin.toString());
    return result;
  }
  
  public String getAccountUpdateUrl(String accountId) {
    String endpointUrl = getUrl(ACCOUNT_ENDPOINT);
    
    String result = endpointUrl + "(" + accountId + ")";
    return result;
  }

  public String getQueueLookupUrl(String queueName) throws ServiceException {
    String endpointUrl = getUrl(QUEUE_ENDPOINT);
    
    String encodedQueueName = encodeValue(queueName);
    String result = endpointUrl + QUEUE_LOOKUP_QUERY_STRING.replace("{name}", encodedQueueName);
    return result;
  }

  public String getQueueItemLookupUrl(String taskId) {
    String endpointUrl = getUrl(QUEUEITEM_ENDPOINT);
    
    String result = endpointUrl + QUEUEITEM_LOOKUP_QUERY_STRING.replace("{taskId}", taskId);
    return result;
  }
  
  public String getValidationErrorLookupUrl(String submissionId) {
    return getValidationErrorUrl() + VALIDATION_ERROR_LOOKUP_QUERY_STRING.replace("{submissionId}", submissionId);
  }
  
  public String getBenefitLookupUrl(String accountId) {
    String endpointUrl = getUrl(BENEFIT_ENDPOINT);
    
    String result = endpointUrl + BENEFIT_LOOKUP_QUERY_STRING.replace("{accountId}", accountId.toString());
    return result;
  }

  public String getProgramYearLookupUrl(Integer year) {
    String endpointUrl = getUrl(PROGRAM_YEAR_ENDPOINT);
    
    String result = endpointUrl + PROGRAM_YEAR_QUERY_STRING.replace("{year}", year.toString());
    return result;
  }

  public String getEnrolmentLookupUrl(String programYearId, String accountId) {
    String endpointUrl = getUrl(ENROLMENT_ENDPOINT);
    
    String result =
        endpointUrl + ENROLMENT_QUERY_STRING
        .replace("{programyearid}", programYearId.toString())
        .replace("{accountid}", accountId.toString());
    return result;
  }

  private String getUrl(String endpointPath) {
    return getAPIUrl() + endpointPath;
  }

  public String getAPIUrl() {
    
    String dynamicsUrl = configUtil.getValue(ConfigurationKeys.CRM_DYNAMICS_URL);
    String apiVersion = configUtil.getValue(ConfigurationKeys.CRM_API_VERSION);
    
    String result = dynamicsUrl + "/api/data/v" + apiVersion + "/";

    return result;
  }

  public boolean isBenefitUrlConfigured() {
    String benefitUrl = getBenefitUpdateUrl();
    return benefitUrl != null;
  }

  public boolean isTaskUrlConfigured() {
    String taskUrl = getTaskUrl();
    return taskUrl != null;
  }

  public boolean isEnrolmentUrlConfigured() {
    String enrolmentUrl = getEnrolmentUpdateUrl();
    return enrolmentUrl != null;
  }

  private String encodeValue(String value) throws ServiceException {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    } catch (UnsupportedEncodingException e) {
      throw new ServiceException(e);
    }
  }
  
}
