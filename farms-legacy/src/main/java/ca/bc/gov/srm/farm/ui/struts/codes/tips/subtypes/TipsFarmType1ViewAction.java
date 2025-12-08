package ca.bc.gov.srm.farm.ui.struts.codes.tips.subtypes;

import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;


public class TipsFarmType1ViewAction extends TipsFarmSubtypeAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping    mapping
   * @param actionForm actionForm
   * @param request    request
   * @param response   response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute
  (final ActionMapping mapping, 
      final ActionForm actionForm,
      final HttpServletRequest request, 
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Farm Type 1...");

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

    CodesService service = ServiceFactory.getCodesService();
    List<FarmSubtype> codes = service.getFarmSubtypesB();
    
    form.setFarmSubtypeBItems(codes);
    form.setNumFarmSubtypes(codes.size());
  }
}

