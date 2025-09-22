/**
 * Copyright (c) 2012, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author awilkinson
 */
public class DirtyFormCheckTag extends TextTagSupport {

  private static final long serialVersionUID = 2618526800333505283L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private String formId;

  /** Creates a new  object. */
  public DirtyFormCheckTag() {
    super();
  }

  /**
   * 
   * @return  The return value.
   */
  @Override
  public String text() {
    StringBuffer html = new StringBuffer();
    
    if(logger.isDebugEnabled()) {
      logger.debug("Registering form for dirty check. Form ID: " + formId);
    }
    
    html.append("<script type=\"text/javascript\">\n");
    html.append("  //<![CDATA[\n");
    html.append("  registerFormForDirtyCheck(document.getElementById(\"");
    html.append(formId);
    html.append("\"));\n");
    html.append("  //]]>\n");
    html.append("</script>");
    
    return html.toString();
  }

  /**
   * @return the formId
   */
  public String getFormId() {
    return formId;
  }

  /**
   * @param formId the formId to set
   */
  public void setFormId(String formId) {
    this.formId = formId;
  }

}
