package ca.bc.gov.srm.farm.ui.struts.enrolment.enrolments;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

/**
 * PinsImportForm.
 *
 */
public class PinsImportForm extends ValidatorForm {

  private static final long serialVersionUID = -1731167803264576595L;

  private FormFile file;


  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
  }

  /**
   * getFile.
   *
   * @return  The return value
   */
  public FormFile getFile() {
    return file;
  }

  /**
   * setFile.
   *
   * @param  file  Set file
   */
  public void setFile(FormFile file) {
    this.file = file;
  }
}
