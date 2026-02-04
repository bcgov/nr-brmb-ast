package ca.bc.gov.srm.farm.ui.struts.codes.tips.subtypes;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.codes.tips.TipFarmTypeIncomeRangeFormData;

public class TipsFarmType2NewAction extends TipsFarmSubtypeAction {
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

    logger.debug("Viewing Create New Farm Subtype...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);  
    TipsFarmSubtypeForm form = (TipsFarmSubtypeForm) actionForm;
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(TipsFarmSubtypeForm form) throws Exception {
    CodesService codesService = ServiceFactory.getCodesService();

    
    List<TipFarmTypeIncomeRange> range = codesService.getFarmType2IncomeRange(-999, -999);
    TipFarmTypeIncomeRangeFormData incomeRangeFormData = new TipFarmTypeIncomeRangeFormData();
    incomeRangeFormData.setIncomeRange(getFormIncomeRangeItems(range));
    
    form.setIncomeRangeJson(jsonObjectMapper.writeValueAsString(incomeRangeFormData));
    form.setIncomeRange(range);
    form.setUsingDefaultRange(true);
    form.setIsInherited(false);
    
    form.setFarmTypeItems(ServiceFactory.getCodesService().getFarmType3s());
    form.setFarmTypeListViewItems(getListItemViews());
    form.setNew(true);
  }
}




