package ca.bc.gov.srm.farm.ui.struts.dataimport;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.apache.commons.lang.StringUtils;

/**
 * ImportForm.
 *
 * @author   $author$
 * @version  $Revision: 2145 $, $Date: 2013-04-11 15:06:51 -0700 (Thu, 11 Apr 2013) $
 */
public class ImportForm extends ValidatorForm {

  private static final long serialVersionUID = -1645502921365403099L;
  
  private String importClassCode;

  private String description;

  private FormFile file;

  private String importVersionId;


  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
  }

  /**
   * getDescription.
   *
   * @return  The return value
   */
  public String getDescription() {
    return description;
  }

  /**
   * setDescription.
   *
   * @param  value  Set description
   */
  public void setDescription(String value) {
    this.description = StringUtils.trimToNull(value);
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

  /**
   * getImportVersionId.
   *
   * @return  The return value
   */
  public String getImportVersionId() {
    return importVersionId;
  }

  /**
   * setImportVersionId.
   *
   * @param  importId  Set importId
   */
  public void setImportVersionId(String importId) {
    this.importVersionId = importId;
  }

	/**
	 * @return the importClassCode
	 */
	public String getImportClassCode() {
		return importClassCode;
	}

	/**
	 * @param importClassCode the importClassCode to set
	 */
	public void setImportClassCode(String importClassCode) {
		this.importClassCode = importClassCode;
	}

}
