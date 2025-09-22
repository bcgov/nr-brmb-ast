package ca.bc.gov.srm.farm.ui.struts;

import org.apache.struts.action.ActionMapping;


/**
 * SecureActionMapping.
 *
 * @author   $author$
 * @version  $Revision: 256 $, $Date: 2009-11-25 16:34:53 -0800 (Wed, 25 Nov 2009) $
 */
public class SecureActionMapping extends ActionMapping {

  /** DOCUMENT ME! */
  private static final long serialVersionUID = 4318920586719811522L;

  /** DOCUMENT ME! */
  private String secureAction;

  /** Creates a new SecureActionMapping object. */
  public SecureActionMapping() {
    super();
  }

  /**
   * getSecureAction.
   *
   * @return  The return value
   */
  public String getSecureAction() {
    return secureAction;
  }

  /**
   * setSecureAction.
   *
   * @param  secureAction  Set secureAction
   */
  public void setSecureAction(String secureAction) {
    this.secureAction = secureAction;
  }
}
