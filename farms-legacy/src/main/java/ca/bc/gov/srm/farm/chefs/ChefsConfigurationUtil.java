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
package ca.bc.gov.srm.farm.chefs;

import static ca.bc.gov.srm.farm.chefs.ChefsConstants.*;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;


public class ChefsConfigurationUtil {

  private static final String SUBMISSIONS_ENDPOINT = "forms/%s/submissions?deleted=false&draft=false";
  private static final String SUBMISSION_ENDPOINT = "submissions/%s";
  private static final String SUBMISSION_OPTIONS_ENDPOINT = "submissions/%s/options";
  private static final String POST_SUBMISSION_ENDPOINT = "forms/%s/versions/%s/submissions";

  private static ChefsConfigurationUtil instance = null;
  
  private static ConfigurationUtility configUtil = ConfigurationUtility.getInstance();

  public static ChefsConfigurationUtil getInstance() {

    if (instance == null) {
      instance = new ChefsConfigurationUtil();
    }

    return instance;
  }

  public ChefsFormCredentials getFormCredentials(String formTypeCode, String formUserType) {
    
    String formIdParameterName = null;
    String apiKeyParameterName = null;
    
    if(formUserType.equals(ChefsConstants.USER_TYPE_IDIR)) {
      
      switch(formTypeCode) {
      case ChefsFormTypeCodes.ADJ:
        formIdParameterName = ConfigurationKeys.CHEFS_ADJUSTMENT_IDIR_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_ADJUSTMENT_IDIR_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.CM:
        formIdParameterName = ConfigurationKeys.CHEFS_CASH_MARGINS_IDIR_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_CASH_MARGINS_IDIR_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.CN:
        formIdParameterName = ConfigurationKeys.CHEFS_COVERAGE_IDIR_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_COVERAGE_IDIR_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.NOL:
        formIdParameterName = ConfigurationKeys.CHEFS_NOL_FORM_IDIR_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_NOL_FORM_IDIR_API_KEY;
        break;
      case ChefsFormTypeCodes.INTERIM:
          formIdParameterName = ConfigurationKeys.CHEFS_INTERIM_IDIR_FORM_GUID;
          apiKeyParameterName = ConfigurationKeys.CHEFS_INTERIM_IDIR_FORM_API_KEY;
          break;
      case ChefsFormTypeCodes.NPP:
        formIdParameterName = ConfigurationKeys.CHEFS_NPP_FORM_IDIR_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_NPP_FORM_IDIR_API_KEY;
        break;
      case ChefsFormTypeCodes.STA:
        formIdParameterName = ConfigurationKeys.CHEFS_STATEMENT_A_IDIR_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_STATEMENT_A_IDIR_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.SUPP:
        formIdParameterName = ConfigurationKeys.CHEFS_SUPPLEMENTAL_IDIR_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_SUPPLEMENTAL_IDIR_FORM_API_KEY;
        break;
      }
      
    } else {
      
      switch(formTypeCode) {
      case ChefsFormTypeCodes.ADJ:
        formIdParameterName = ConfigurationKeys.CHEFS_ADJUSTMENT_BASIC_BCEID_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_ADJUSTMENT_BASIC_BCEID_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.CM:
        formIdParameterName = ConfigurationKeys.CHEFS_CASH_MARGINS_BASIC_BCEID_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_CASH_MARGINS_BASIC_BCEID_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.CN:
        formIdParameterName = ConfigurationKeys.CHEFS_COVERAGE_BASIC_BCEID_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_COVERAGE_BASIC_BCEID_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.NOL:
        formIdParameterName = ConfigurationKeys.CHEFS_NOL_FORM_BASIC_BCEID_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_NOL_FORM_BASIC_BCEID_API_KEY;
        break;
      case ChefsFormTypeCodes.INTERIM:
          formIdParameterName = ConfigurationKeys.CHEFS_INTERIM_BASIC_BCEID_FORM_GUID;
          apiKeyParameterName = ConfigurationKeys.CHEFS_INTERIM_BASIC_BCEID_FORM_API_KEY;
          break;
      case ChefsFormTypeCodes.NPP:
        formIdParameterName = ConfigurationKeys.CHEFS_NPP_FORM_BASIC_BCEID_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_NPP_FORM_BASIC_BCEID_API_KEY;
        break;
      case ChefsFormTypeCodes.STA:
        formIdParameterName = ConfigurationKeys.CHEFS_STATEMENT_A_BASIC_BCEID_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_STATEMENT_A_BASIC_BCEID_FORM_API_KEY;
        break;
      case ChefsFormTypeCodes.SUPP:
        formIdParameterName = ConfigurationKeys.CHEFS_SUPPLEMENTAL_BASIC_BCEID_FORM_GUID;
        apiKeyParameterName = ConfigurationKeys.CHEFS_SUPPLEMENTAL_BASIC_BCEID_FORM_API_KEY;
        break;
      }
      
    }
    
    String formId = configUtil.getValue(formIdParameterName);
    String apiKey = configUtil.getValue(apiKeyParameterName);
    
    ChefsFormCredentials creds = new ChefsFormCredentials(formId, apiKey);
    return creds;
  }

  public String getApiUrl() {
    
    String chefsUrl = configUtil.getValue(ConfigurationKeys.CHEFS_URL);
    String apiVersion = configUtil.getValue(ConfigurationKeys.CHEFS_API_VERSION);

      return chefsUrl + "/" + apiVersion + "/";
  }

  public String getSubmissionsUrl(String formId) {
    String url = getApiUrl() + String.format(SUBMISSIONS_ENDPOINT, formId);
    
    return url;
  }

  public String getSubmissionUrl(String submissionGuid) {
    String url = getApiUrl() + String.format(SUBMISSION_ENDPOINT, submissionGuid);
    
    return url;
  }
  
  public String getSubmissionOptionsUrl(String submissionGuid) {
    String url = getApiUrl() + String.format(SUBMISSION_OPTIONS_ENDPOINT, submissionGuid);
    
    return url;
  }
  
  public String postSubmissionUrl(String formId, String formVersionId) {
    return getApiUrl() + String.format(POST_SUBMISSION_ENDPOINT, formId, formVersionId);
  }

  public String getViewSubmissionUrl(String submissionGuid) {
    StringBuilder urlBuilder = new StringBuilder();
    urlBuilder.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    urlBuilder.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    urlBuilder.append(submissionGuid);
    String chefsSubmissionUrl = urlBuilder.toString();
    return chefsSubmissionUrl;
  }
  
}
