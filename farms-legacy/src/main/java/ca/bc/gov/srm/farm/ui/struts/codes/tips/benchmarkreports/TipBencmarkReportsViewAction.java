/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarkreports;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.tips.TipBenchmarkGroup;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * @author awilkinson
 */
public class TipBencmarkReportsViewAction extends SecureAction {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  @Override
  protected ActionForward doExecute
  (final ActionMapping mapping, 
      final ActionForm actionForm,
      final HttpServletRequest request, 
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Benchmark Reports screen...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipBencmarkReportsForm form = (TipBencmarkReportsForm) actionForm;
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    Map<Integer, Map<Integer, Map<String, List<TipBenchmarkGroup>>>> benchmarkGroupsByYear = tipReportService.getBenchmarkGroups();

    populateForm(form, benchmarkGroupsByYear);

    return forward;
  }

  private void populateForm(
      TipBencmarkReportsForm form,
      Map<Integer, Map<Integer, Map<String, List<TipBenchmarkGroup>>>> benchmarkGroupsByYear)
      throws JsonProcessingException {
    
    String benchmarkGroupsJson = jsonObjectMapper.writeValueAsString(benchmarkGroupsByYear);
    form.setBenchmarkGroupsJson(benchmarkGroupsJson);
    
  }
}
