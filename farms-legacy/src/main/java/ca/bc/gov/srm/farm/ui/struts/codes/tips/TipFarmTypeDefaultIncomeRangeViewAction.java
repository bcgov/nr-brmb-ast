package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

public class TipFarmTypeDefaultIncomeRangeViewAction extends SecureAction {
  private Logger logger = LoggerFactory.getLogger(getClass());
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Farm Type Default Income Ranges...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);    
    TipFarmTypeDefaultIncomeRangeForm form = (TipFarmTypeDefaultIncomeRangeForm) actionForm;
    populateForm(form);

    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(TipFarmTypeDefaultIncomeRangeForm form) throws Exception {
    
    CodesService codesService = ServiceFactory.getCodesService();
    
    List<TipFarmTypeIncomeRange> range = codesService.getFarmTypeDefaultIncomeRange();
    TipFarmTypeIncomeRangeFormData incomeRangeFormData = new TipFarmTypeIncomeRangeFormData();
    incomeRangeFormData.setIncomeRange(TipIncomeRangeFormDataUtil.getFormIncomeRangeItems(range));
    
    form.setIncomeRangeJson(jsonObjectMapper.writeValueAsString(incomeRangeFormData));
    form.setIncomeRange(range);
  }
}
