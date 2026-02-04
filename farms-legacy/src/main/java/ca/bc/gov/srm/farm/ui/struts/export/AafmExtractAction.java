package ca.bc.gov.srm.farm.ui.struts.export;

import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.security.SecurityRule;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.service.ExportService;
import ca.bc.gov.srm.farm.service.impl.ExportServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.http.HttpRequestUtils;


/**
 * AafmExtractAction - The extract returns a zip, so we can't use
 * Oracle reports. Generate the zip and return it as a stream to the browser.
 */
public class AafmExtractAction extends SecureAction {
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * doExecute.
   *
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   *
   * @return  The return value
   *
   * @throws  Exception  On Exception
   */
  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
    log.debug("<doExecute");

    if (!isTokenValid(request, true)) {
      ActionMessages errors = new ActionMessages();
      String key = MessageConstants.ERRORS_DUPLICATE_SUBMIT;
      errors.add("", new ActionMessage(key));
      addErrors(request, errors);
      return mapping.findForward(ActionConstants.FAILURE);
    }

    ExportForm form = (ExportForm) actionForm;
    Integer year = new Integer(form.getYear());
    String exportType=form.getExportType();
    String userAccountName = getUserAccountName();
    Connection conn = null;
    
    try {
      conn = openConnection(request);

      ExportService service = ExportServiceFactory.getInstance(conn);
      service.exportNumberedFilesZip(year, exportType, userAccountName);
      
    } finally {
      closeConnection(conn);
    }

    log.debug(">doExecute");
    return null;
  }
  
  
  /**
   * @return  Webade Connection
   * @param request request
   * @throws  Exception  on exception
   */
  private Connection openConnection(final HttpServletRequest request) 
  throws Exception {
    log.debug("<openConnection");
    BusinessAction ba = BusinessAction.system();
    SecurityRule rule = SecurityUtility.getInstance().getSecurityRule(ba);
    Action action = new Action(rule.getRuleName());
    ServletContext context = request.getSession().getServletContext();
    Application application = HttpRequestUtils.getApplication(context);
    Connection conn = application.getConnectionByPriviledgedAction(action);

    log.debug(">openConnection");
    return conn;
  }

  
  /**
   * @param  connection  connection
   */
  private void closeConnection(Connection connection) {
    log.debug("<closeConnection");
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception ex) {
        log.error("Error closing connection", ex);
      }
    }
    log.debug(">closeConnection");
  }
  
  
}
