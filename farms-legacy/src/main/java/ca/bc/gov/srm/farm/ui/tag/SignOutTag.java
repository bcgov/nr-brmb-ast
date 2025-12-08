/**
 * Copyright (c) 2009, B.C. Government.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import org.apache.commons.lang.StringEscapeUtils;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;


/**
 * SignOutTag.
 */
public class SignOutTag extends TextTagSupport {

  private static final long serialVersionUID = -2627378921365403105L;


  /** Creates a new  object. */
  public SignOutTag() {
    super();
  }

  /**
   * 
   * @return  The return value.
   */
  @Override
  public String text() {
    ConfigurationUtility config = ConfigurationUtility.getInstance();
    String key = ConfigurationKeys.SIGN_OUT_URL;
    String url = config.getValue(key);
    url = StringEscapeUtils.escapeXml(url);
    String html = "<a href=\"" + url + "\">Sign Out</a>";
    
    return html;
  }

}
