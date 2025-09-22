package ca.bc.gov.srm.farm.ui.struts.codes.reasonability;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCode;

public class FruitVegCodeViewAction extends FruitVegCodeAction {
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

    logger.debug("Viewing Fruit Veg Item Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FruitVegCodeForm form = (FruitVegCodeForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(FruitVegCodeForm form) throws Exception {
    
    CodesService codesService = ServiceFactory.getCodesService();
    List<FruitVegTypeCode> codes = codesService.getFruitVegCodes();
    form.setFruitVegCodes(codes);
    form.setNumFruitVegCodes(codes.size());
  }
}
