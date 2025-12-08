/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.generic;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * 
 * @author awilkinson
 *
 */
public class CodeActionUtils {

  private CodeActionUtils() {
    // empty
  }
  
  public static ActionRedirect getSuccessRedirect(ActionMapping mapping, CodesForm form) {

    ActionRedirect redirect = new ActionRedirect(mapping.findForward(ActionConstants.SUCCESS));
    redirect.addParameter("codeTable", form.getCodeTable());

    return redirect;
  }

}
