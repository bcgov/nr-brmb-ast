package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class TipsFarmType3ViewAction extends TipsFarmType3Action {
  private Logger logger = LoggerFactory.getLogger(getClass());

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

    logger.debug("Viewing Farm Type Item Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    TipsFarmType3Form form = (TipsFarmType3Form) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(TipsFarmType3Form form) throws Exception {
    
    CodesService codesService = ServiceFactory.getCodesService();
    FarmType3 code = codesService.getFarmTypeCode(form.getId());
    
    if (code == null) {
    	return;
    }
    
    List<TipFarmTypeIncomeRange> range = codesService.getFarmType3IncomeRange(code.getFarmTypeId());
    TipFarmTypeIncomeRangeFormData incomeRangeFormData = new TipFarmTypeIncomeRangeFormData();
    incomeRangeFormData.setIncomeRange(TipIncomeRangeFormDataUtil.getFormIncomeRangeItems(range));
    boolean isDefault = range.get(0).getIsDefault();
    
    form.setIncomeRangeJson(jsonObjectMapper.writeValueAsString(incomeRangeFormData));
    form.setIncomeRange(range);
    form.setUsingDefaultRange(isDefault);
    form.setRevisionCount(code.getRevisionCount());
    form.setId(code.getFarmTypeId());
    form.setFarmTypeName(code.getFarmTypeName());
  }
}
