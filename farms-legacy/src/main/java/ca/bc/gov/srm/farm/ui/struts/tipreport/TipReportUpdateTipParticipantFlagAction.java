package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;

public class TipReportUpdateTipParticipantFlagAction extends SecureAction {

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipReportForm form = (TipReportForm) actionForm;
    String userId = CurrentUser.getUser().getUserId();
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();

    Collection<Integer> participantPins = new ArrayList<>();
    String participantPinsString = form.getParticipantPins();
    String[] participantPinsArray = participantPinsString.split(",");
    for(String pinString : participantPinsArray) {
      Integer participantPin = Integer.parseInt(pinString);
      participantPins.add(participantPin);
    }
    
    Boolean isTipParticipant = form.getIsTipParticipant();
    tipReportService.updateTipParticipantFlag(participantPins, isTipParticipant , userId);
    
    String responseJson = "{\"success\":true}";

    IOUtils.writeJsonToResponse(response, responseJson);

    return forward;
  }
}
