package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.service.EnrolmentService;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments.PinsImportForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.IOUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * PinsImportAction. Used by screen 361 to start an "import".
 */
public class PinsImportAction extends SecureAction {

  /**
   * execute.
   * 
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   * 
   * @return  The return value
   * 
   * @throws  Exception   On Exception
   */
  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm,
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionMessages errors = actionForm.validate(mapping, request);
    if(errors == null) {
      errors = new ActionMessages();
    }
    if (!errors.isEmpty()) {
      saveErrors(request, errors);
      return mapping.findForward(ActionConstants.FAILURE);
    }

    PinsImportForm form = (PinsImportForm) actionForm;
    EnrolmentService enrolmentService = ServiceFactory.getEnrolmentService();

    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new InputStreamReader(form.getFile().getInputStream()));
      int programYear = Integer.parseInt(bufferedReader.readLine());
      boolean createTaskInBarn = Boolean.parseBoolean(bufferedReader.readLine());
      String[] pinStrings = bufferedReader.readLine().split(",");
      int[] pins = new int[pinStrings.length];
      for (int i = 0; i < pinStrings.length; i++) {
        pins[i] = Integer.parseInt(pinStrings[i]);
      }

      for (int pin : pins) {
        Scenario scenario = enrolmentService.getScenario(pin, programYear);
        if (scenario == null || scenario.getClient() == null) {
          String msgKey = MessageConstants.ERRORS_IMPORT_INVALID_PIN;
          String[] msgParameters = new String[]{String.valueOf(pin), String.valueOf(programYear)};
          errors.add("", new ActionMessage(msgKey, msgParameters));
          break;
        }
      }

      File pTempDir = IOUtils.getTempDir();

      File outFile = File.createTempFile("farm_enrolment_", ".csv", pTempDir);
      try (FileOutputStream fos = new FileOutputStream(outFile);
           PrintWriter pw = new PrintWriter(fos);) {

        pw.println(String.valueOf(programYear));
        pw.println(String.valueOf(createTaskInBarn));
        pw.println(StringUtils.join(pinStrings, ","));
        pw.flush();
      }

      ImportService importService = ServiceFactory.getImportService();

      try (FileInputStream importFileInputStream = new FileInputStream(outFile);) {
        ImportVersion importVersion = importService.createImportVersion(
            ImportClassCodes.ENROL,
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            "Uploaded list of PINs",
            outFile.getPath(),
            importFileInputStream,
            CurrentUser.getUser().getUserId());

        String key = CacheKeys.CURRENT_IMPORT;
        CacheFactory.getUserCache().addItem(key, importVersion);
      }
    } catch (IOException e) {
      String key = MessageConstants.ERRORS_IMPORT;
      errors.add("", new ActionMessage(key));
    } catch (NullPointerException e) {
      String key = MessageConstants.ERRORS_IMPORT_INVALID_FORMAT;
      errors.add("", new ActionMessage(key));
    } catch (NumberFormatException e) {
      String key = MessageConstants.ERRORS_IMPORT_INVALID_FORMAT;
      errors.add("", new ActionMessage(key));
    } finally {
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
          // do nothing
        }
      }
    }

    if (!errors.isEmpty()) {
      addErrors(request, errors);
      return mapping.findForward(ActionConstants.FAILURE);
    }

    return mapping.findForward(ActionConstants.SUCCESS);
  }
}
