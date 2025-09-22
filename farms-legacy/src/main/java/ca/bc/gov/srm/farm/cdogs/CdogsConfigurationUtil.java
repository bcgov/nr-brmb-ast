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
package ca.bc.gov.srm.farm.cdogs;

import static ca.bc.gov.srm.farm.cdogs.CdogsConstants.*;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;

public class CdogsConfigurationUtil {

	private static CdogsConfigurationUtil instance = null;
	private static final ConfigurationUtility configUtil = ConfigurationUtility.getInstance();

	public static CdogsConfigurationUtil getInstance() {

		if (instance == null) {
			instance = new CdogsConfigurationUtil();
		}

		return instance;
	}

	public String getHealthCheckUrl() {
		return getUrl(HEALTH_CHECK_ENDPOINT);
	}

	public String getFileTypeUrl() {
		return getUrl(FILE_TYPES_ENDPOINT);
	}
	
	public String getExistingTemplateUrl(String templateUid) {
		return getUrl(String.format(EXISTING_TEMPLATE_ENDPOINT, templateUid));
	}
	
	public String getExistingTemplateCacheUrl(String templateUid) {
    return getUrl(String.format(EXISTING_TEMPLATE_CACHE_ENDPOINT, templateUid));
  }
	private String getUrl(String endpointPath) {
		return getAPIUrl() + endpointPath;
	}

	public String getAPIUrl() {

    String baseUrl = configUtil.getValue(ConfigurationKeys.CDOGS_BASE_URL);
    String apiVersion = configUtil.getValue(ConfigurationKeys.CDOGS_API_VERSION);

        return baseUrl + "/api/v" + apiVersion + "/";
	}
	
	public String getAdjustmentTemplateGuid() {
		return configUtil.getValue(ConfigurationKeys.CDOGS_ADJUSTMENT_TEMPLATE_GUID);
	}

	public String getCashMarginsTemplateGuid() {
	  return configUtil.getValue(ConfigurationKeys.CDOGS_CASH_MARGINS_TEMPLATE_GUID);
	}

	public String getCoverageTemplateGuid() {
	  return configUtil.getValue(ConfigurationKeys.CDOGS_COVERAGE_TEMPLATE_GUID);
	}
	
	public String getInterimTemplateGuid(Integer version) {
	  if (version != null && version == 2) {
	    return configUtil.getValue(ConfigurationKeys.CDOGS_INTERIM_TEMPLATE_GUID_V2);
	  } 
	  return configUtil.getValue(ConfigurationKeys.CDOGS_INTERIM_TEMPLATE_GUID);
	}
	
	public String getNolTemplateGuid() {
		return configUtil.getValue(ConfigurationKeys.CDOGS_NOL_TEMPLATE_GUID);
	}
	
	public String getNppTemplateGuid() {
		return configUtil.getValue(ConfigurationKeys.CDOGS_NPP_TEMPLATE_GUID);
	}
	
	public String getStatementATemplateGuid() {
	    return configUtil.getValue(ConfigurationKeys.CDOGS_STATEMENT_A_TEMPLATE_GUID);
	}

	public String getSupplementalTemplateGuid() {
	  return configUtil.getValue(ConfigurationKeys.CDOGS_SUPPLEMENTAL_TEMPLATE_GUID);
	}

	public String getCoverageNoticeReportTemplateGuid() {
    return configUtil.getValue(ConfigurationKeys.CDOGS_COVERAGE_REPORT_TEMPLATE_GUID);
  }

}
