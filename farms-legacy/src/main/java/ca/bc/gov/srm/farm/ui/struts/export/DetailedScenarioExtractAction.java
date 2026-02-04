package ca.bc.gov.srm.farm.ui.struts.export;

import java.io.File;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.security.SecurityRule;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.service.ExportService;
import ca.bc.gov.srm.farm.service.impl.ExportServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.http.HttpRequestUtils;


/**
 * Generate CSV and return it as a stream to the browser.
 */
public class DetailedScenarioExtractAction extends SecureAction {
	private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public ActionForward doExecute(
  	final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {
    logger.debug("<doExecute");

    ExportForm form = (ExportForm) actionForm;
    Integer year = new Integer(form.getYear());
    Connection conn = null;
    
    File outputFile;
    try {
      conn = openConnection(request);
      
      File tempDir = IOUtils.getTempDir();  
      ExportService service = ExportServiceFactory.getInstance(conn);
      outputFile = service.exportDetailedScenarioExtract(year, tempDir);
      
    } finally {
      closeConnection(conn);
    }

    
    IOUtils.writeFileToResponse(response, outputFile, IOUtils.CONTENT_TYPE_CSV);

    logger.debug(">doExecute");
    return null;
  }
  
  
  /**
   * @return  Webade Connection
   * @param request request
   * @throws  Exception  on exception
   */
  private Connection openConnection(final HttpServletRequest request) 
  throws Exception {
    logger.debug("<openConnection");
    BusinessAction ba = BusinessAction.system();
    SecurityRule rule = SecurityUtility.getInstance().getSecurityRule(ba);
    Action action = new Action(rule.getRuleName());
    ServletContext context = request.getSession().getServletContext();
    Application application = HttpRequestUtils.getApplication(context);
    Connection conn = application.getConnectionByPriviledgedAction(action);

    logger.debug(">openConnection");
    return conn;
  }

  
  /**
   * @param  connection  connection
   */
  private void closeConnection(Connection connection) {
    logger.debug("<closeConnection");
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception e) {
        logger.error("Unexpected error: ", e);
      }
    }
    logger.debug(">closeConnection");
  }
 
}
