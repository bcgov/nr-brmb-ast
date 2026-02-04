package ca.bc.gov.srm.farm.ui.struts.codes.tips.subtypes;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.codes.tips.TipFarmTypeIncomeRangeFormData;

public class TipsFarmType2ItemViewAction extends TipsFarmSubtypeAction {
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

    logger.debug("Viewing Farm Subtype Item Code...");

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
    FarmSubtype code = codesService.getFarmSubtypeA(form.getId());
    
    if (code == null) {
        return;
    }
    
    List<TipFarmTypeIncomeRange> range = codesService.getFarmType2IncomeRange(code.getId(), code.getParentId());
    TipFarmTypeIncomeRangeFormData incomeRangeFormData = new TipFarmTypeIncomeRangeFormData();
    incomeRangeFormData.setIncomeRange(getFormIncomeRangeItems(range));
    
    form.setIncomeRangeJson(jsonObjectMapper.writeValueAsString(incomeRangeFormData));
    form.setIncomeRange(range);
    form.setUsingDefaultRange(range.get(0).getIsDefault());
    form.setIsInherited(range.get(0).getIsInherited());

    if (form.getIsInherited() != null && form.getIsInherited()) {
      form.setInheritedFrom(code.getParentName());
    }

    form.setFarmTypeListViewItems(getListItemViews());
    form.setRevisionCount(code.getRevisionCount());       
    form.setName(code.getName());
    form.setParentName(code.getParentName());
    form.setId(code.getId());
    form.setParentId(code.getParentId());
    form.setSecondaryParentId(code.getSecondaryParentId());
    
  }
}
